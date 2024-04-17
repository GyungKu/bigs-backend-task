package com.example.bigsbackendtask.service

import com.example.bigsbackendtask.entity.Weather
import com.example.bigsbackendtask.exception.ErrorCode
import com.example.bigsbackendtask.exception.GlobalException
import com.example.bigsbackendtask.repository.WeatherRepository
import org.springframework.stereotype.Service

@Service
class WeatherReadService(private val weatherRepository: WeatherRepository) {

    fun getWeathers(): List<Weather> {
        val weathers = weatherRepository.findAll()
        if (weathers.isEmpty()) {
            throw GlobalException(ErrorCode.NO_CONTENT.status, ErrorCode.NO_CONTENT.message)
        }
        return weathers
    }
}