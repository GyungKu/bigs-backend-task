package com.example.bigsbackendtask.repository

import com.example.bigsbackendtask.entity.Weather
import org.springframework.data.jpa.repository.JpaRepository

interface WeatherRepository: JpaRepository<Weather, Long> {
}