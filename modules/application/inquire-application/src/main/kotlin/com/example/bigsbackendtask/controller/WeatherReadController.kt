package com.example.bigsbackendtask.controller

import com.example.bigsbackendtask.entity.Weather
import com.example.bigsbackendtask.service.WeatherReadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/weathers")
class WeatherReadController(private val weatherReadService: WeatherReadService) {

    @GetMapping
    fun getWeathers(): ResponseEntity<List<Weather>> {
        return ResponseEntity.ok(weatherReadService.getWeathers())
    }

}