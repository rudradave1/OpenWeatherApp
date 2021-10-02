package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class CoordX(
    val lat: Double,
    val lon: Double
)