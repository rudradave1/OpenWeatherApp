package com.rudra.weatherinformationapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Weather(
    @ColumnInfo(name = "base") val base: String,
    @ColumnInfo(name = "clouds") val clouds: Clouds,
    @ColumnInfo(name = "cod") val cod: Int,
    @ColumnInfo(name = "coord") val coord: Coord,
    @ColumnInfo(name = "dt") val dt: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "main") val main: Main,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sys") val sys: Sys,
    @ColumnInfo(name = "timezone") val timezone: Int,
    @ColumnInfo(name = "visibility") val visibility: Int,
    @ColumnInfo(name = "weather") val weather: List<WeatherX>,
    @ColumnInfo(name = "wind") val wind: Wind
)