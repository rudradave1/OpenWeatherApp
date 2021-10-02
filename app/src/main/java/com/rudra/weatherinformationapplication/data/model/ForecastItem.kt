package com.rudra.weatherinformationapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class ForecastItem (
    @ColumnInfo(name = "clouds") val clouds: CloudsX,
    @ColumnInfo(name = "dt") val dt: Int,
    @ColumnInfo(name = "dt_txt") val dt_txt: String,
    @ColumnInfo(name = "main") val main: MainX,
    @ColumnInfo(name = "pop") val pop: Double,
    @ColumnInfo(name = "rain") val rain: Rain,
    @ColumnInfo(name = "sys") val sys: SysX,
    @ColumnInfo(name = "visibility") val visibility: Int,
    @ColumnInfo(name = "weather") val weather: List<WeatherXX>,
    @ColumnInfo(name = "wind") val wind: WindX
)