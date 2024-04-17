package com.example.bigsbackendtask.controller

import com.example.bigsbackendtask.service.WeatherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/weathers")
class WeatherController(private val weatherService: WeatherService) {

    @PostMapping
    fun saveWeather(): ResponseEntity<String> {
        weatherService.saveWeather()
        return ResponseEntity.ok().build()
    }

}