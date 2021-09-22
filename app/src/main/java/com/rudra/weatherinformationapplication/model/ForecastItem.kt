package com.rudra.weatherinformationapplication.model

data class ForecastItem (
    val clouds: CloudsX,
    val dt: Int,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val rain: Rain,
    val sys: SysX,
    val visibility: Int,
    val weather: List<WeatherXX>,
    val wind: WindX
)