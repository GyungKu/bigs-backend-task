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
    var id: Long? = null
    var baseDate: String = baseDate
    var baseTime: String = baseTime
    var category: String = category
    var fcstDate: String = fcstDate
    var fcstValue: String = fcstValue
    var nx: Int = nx
    var ny: Int = ny
}