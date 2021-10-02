package com.rudra.weatherinformationapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudra.weatherinformationapplication.data.model.Weather

@Database(entities = [Weather::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao() : WeatherDao
}