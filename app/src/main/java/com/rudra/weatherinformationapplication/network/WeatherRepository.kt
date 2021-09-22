package com.rudra.weatherinformationapplication.network

class WeatherRepository constructor(private val weatherService: WeatherService, private val lat: String, private val lon: String,  private val appid: String) {
    fun getWeather() = weatherService.getWeather(lat, lon, appid)
}