package com.rudra.weatherinformationapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Clouds(
    @ColumnInfo(name = "all") val all: String
)