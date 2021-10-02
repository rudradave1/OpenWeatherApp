package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class WindX(
    val deg: Int,
    val gust: Double,
    val speed: Double
)