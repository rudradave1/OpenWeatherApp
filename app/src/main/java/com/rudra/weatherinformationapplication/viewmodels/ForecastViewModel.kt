package com.rudra.weatherinformationapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudra.weatherinformationapplication.model.ForecastItem
import com.rudra.weatherinformationapplication.network.ForecastRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastViewModel  constructor(private val repository: ForecastRepository)  : ViewModel() {
    val weatherList = MutableLiveData<List<ForecastItem>>()
    val errorMessage = MutableLiveData<String>()

    fun getWeather() {
        val response = repository.getForecast()

        response.enqueue(object : Callback<List<ForecastItem>> {
            override fun onResponse(call: Call<List<ForecastItem>>, response: Response<List<ForecastItem>>) {
                weatherList.postValue(response.body())
            }
            override fun onFailure(call: Call<List<ForecastItem>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}