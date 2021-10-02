package com.rudra.weatherinformationapplication.data.model

import androidx.room.Entity

@Entity
data class Sys(
    val country: String,
    val sunrise: String,
    val sunset: String
)