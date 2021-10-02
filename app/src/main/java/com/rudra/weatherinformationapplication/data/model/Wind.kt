package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class Wind(
    val deg: String,
    val gust: String,
    val speed: String
)