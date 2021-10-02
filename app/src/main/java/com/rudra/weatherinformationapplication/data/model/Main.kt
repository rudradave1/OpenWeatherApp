package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class Main(
    val feels_like: String,
    val grnd_level: String,
    val humidity: String,
    val pressure: String,
    val sea_level: String,
    val temp: String,
    val temp_max: String,
    val temp_min: String
)