package com.rudra.weatherinformationapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudra.weatherinformationapplication.model.Weather
import com.rudra.weatherinformationapplication.network.WeatherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel constructor(private val repository: WeatherRepository)  : ViewModel() {
    val weatherList = MutableLiveData<Weather>()
    val errorMessage = MutableLiveData<String>()

    fun getWeather() {
        val response = repository.getWeather()

        response.enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                weatherList.postValue(response.body())
            }
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }
}