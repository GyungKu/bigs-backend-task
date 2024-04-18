package com.example.bigsbackendtask.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Weather(baseDate: String,
              baseTime: String,
              category: String,
              fcstDate: String,
              fcstValue: String,
              nx: Int,
              ny: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val baseDate: String = baseDate
    val baseTime: String = baseTime
    val category: String = category
    val fcstDate: String = fcstDate
    val fcstValue: String = fcstValue
    val nx: Int = nx
    val ny: Int = ny
}