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
        return when (hour) {
            in 2..4 -> "0200"
            in 5..7 -> "0500"
            in 8..10 -> "0800"
            in 11..13 -> "1100"
            in 14..16 -> "1400"
            in 17..19 -> "1700"
            in 20..22 -> "2000"
            else -> "2300"
        }
    }

    private fun getBaseDate(now: LocalDateTime, hour: Int): String {
        val year = now.year.toString()
        val month = twoDigit(now.monthValue)
        val day = twoDigit(if(hour < 2) now.minusDays(1).dayOfMonth else now.dayOfMonth)

        return "$year$month$day"
    }

    private fun twoDigit(num: Int): String {
        return if (num < 10) "0$num" else num.toString()
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
                    baseDate = item["baseDate"].asText(),
                    baseTime = item["baseTime"].asText(),
                    category = item["category"].asText(),
                    fcstDate = item["fcstDate"].asText(),
                    fcstValue = item["fcstValue"].asText(),
                    nx = item["nx"].asInt(),
                    ny = item["ny"].asInt()
            )
        }
    }

}