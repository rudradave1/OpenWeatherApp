package com.rudra.weatherinformationapplication.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Room
import com.rudra.weatherinformationapplication.data.model.Forecast
import com.rudra.weatherinformationapplication.data.model.ForecastItem
import com.rudra.weatherinformationapplication.data.model.Weather
import com.rudra.weatherinformationapplication.data.model.WeatherXX

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weatherxx")
    fun getForecastHistory(): List<ForecastItem>


    @Query("SELECT * FROM forecastitem")
    fun getCurrentWeatherHistory(): List<Weather>

}