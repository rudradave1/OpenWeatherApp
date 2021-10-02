package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class Coord(
    val lat: String,
    val lon: String
)