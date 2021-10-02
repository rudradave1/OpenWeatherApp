package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastItem>,
    val message: Int
)