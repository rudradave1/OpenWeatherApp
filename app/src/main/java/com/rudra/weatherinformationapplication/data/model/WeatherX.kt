package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class WeatherX(
    val description: String,
    val icon: String,
    val id: String,
    val main: String
)