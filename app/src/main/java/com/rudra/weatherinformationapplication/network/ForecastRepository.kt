package com.rudra.weatherinformationapplication.network

class ForecastRepository constructor(private val weatherService: WeatherService, private val lat: String, private val lon: String,  private val appid: String) {
    fun getForecast() = weatherService.getForecast(lat, lon, appid)
}