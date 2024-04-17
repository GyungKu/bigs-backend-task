package com.example.bigsbackendtask.service

import com.example.bigsbackendtask.entity.Weather
import com.example.bigsbackendtask.exception.ErrorCode.*
import com.example.bigsbackendtask.exception.GlobalException
import com.example.bigsbackendtask.repository.WeatherRepository
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDateTime

@Service
class WeatherService(
        private val weatherRepository: WeatherRepository,
        private val restTemplate: RestTemplate,
        private val objectMapper: ObjectMapper,
        @Value("\${open-api.service-key}")  private val serviceKey: String
) {

    companion object {
        private const val NX = 62
        private const val NY = 130
        private const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
    }

    fun saveWeather() {
        val now = LocalDateTime.now()
        val hour = now.hour

        val baseTime: String = getBaseTime(hour)
        val baseDate: String = getBaseDate(now, hour)
        val uri = getUri(baseTime, baseDate)
        val res = fetchDataFromApi(uri)
        weatherRepository.saveAll(parseData(res))
    }

    private fun getBaseTime(hour: Int): String {
        var baseTime = ""

        when (hour) {
            in 2..4 -> baseTime = "0200"
            in 5..7 -> baseTime = "0500"
            in 8..10 -> baseTime = "0800"
            in 11..13 -> baseTime = "1100"
            in 14..16 -> baseTime = "1400"
            in 17..19 -> baseTime = "1700"
            in 20..22 -> baseTime = "2000"
            else -> baseTime = "2300"
        }
        return baseTime
    }

    private fun getBaseDate(now: LocalDateTime, hour: Int): String {
        val year = now.year.toString()
        var month = now.monthValue.toString()
        var day = if(hour < 2) (now.dayOfMonth -1).toString() else now.dayOfMonth.toString()

        if (month.toInt() < 10) month = "0$month"
        if (day.toInt() < 10) day = "0$day"

        return year + month + day
    }

    private fun getUri(baseTime: String, baseDate: String) = UriComponentsBuilder
            .fromHttpUrl(BASE_URL)
            .queryParam("ServiceKey", serviceKey)
            .queryParam("pageNo", "1")
            .queryParam("numOfRows", "1000")
            .queryParam("dataType", "JSON")
            .queryParam("base_time", baseTime)
            .queryParam("base_date", baseDate)
            .queryParam("nx", NX)
            .queryParam("ny", NY)
            .encode()
            .build()
            .toUri()

    private fun fetchDataFromApi(uri: URI): String {
        return restTemplate.getForObject(uri, String::class.java)
                ?: throw GlobalException(FAILED_DATA_FETCH_EXTERNAL_API.status, FAILED_DATA_FETCH_EXTERNAL_API.message)
    }

    private fun parseData(res: String): List<Weather> {
        val items = try {
            objectMapper.readTree(res).get("response").get("body").get("items").get("item")
        } catch (e: JsonParseException) {
            throw GlobalException(JSON_PARSING_FAIL.status, JSON_PARSING_FAIL.message)
        }

        return items.map { item ->
            Weather(
                    item["baseDate"].asText(),
                    item["baseTime"].asText(),
                    item["category"].asText(),
                    item["fcstDate"].asText(),
                    item["fcstValue"].asText(),
                    item["nx"].asInt(),
                    item["ny"].asInt()
            )
        }
    }

}