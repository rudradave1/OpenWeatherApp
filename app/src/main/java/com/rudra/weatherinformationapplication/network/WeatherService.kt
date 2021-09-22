package com.rudra.weatherinformationapplication.network

import com.rudra.weatherinformationapplication.model.Forecast
import com.rudra.weatherinformationapplication.model.ForecastItem
import com.rudra.weatherinformationapplication.model.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather/")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") query: String): Call<Weather>

    @GET("forecast/")
    fun getForecast(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") query: String): Call<List<ForecastItem>>

    companion object {
        private var weatherService: WeatherService? = null
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun getInstance() : WeatherService {
            if (weatherService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                weatherService = retrofit.create(WeatherService::class.java)
            }
            return weatherService!!
        }
    }
}