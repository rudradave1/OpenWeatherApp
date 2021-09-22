package com.rudra.weatherinformationapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rudra.weatherinformationapplication.network.ForecastRepository
import com.rudra.weatherinformationapplication.network.WeatherRepository

class ForecastViewModelFactory constructor(private val repository: ForecastRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
            ForecastViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}