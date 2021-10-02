package com.rudra.weatherinformationapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudra.weatherinformationapplication.R
import com.rudra.weatherinformationapplication.network.WeatherRepository
import com.rudra.weatherinformationapplication.network.WeatherService
import com.rudra.weatherinformationapplication.viewmodels.WeatherViewModel
import com.rudra.weatherinformationapplication.viewmodels.WeatherViewModelFactory

class CurrentWeatherFragment(private val lat: String, private val lon: String) : Fragment() {

    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var viewModel: WeatherViewModel
    private val retrofitService = WeatherService.getInstance()
    private val TAG = "CurrentWeatherFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_weather, container, false)

        titleTextView = view.findViewById(R.id.titleTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this, WeatherViewModelFactory(WeatherRepository(retrofitService, lat, lon, getString(R.string.open_weather_api_key)))).get(WeatherViewModel::class.java)
        viewModel.weatherList.observe(requireActivity(), Observer {
            Log.d(TAG, "onCreate: $it")
            titleTextView.text = it.weather[0].main
            descriptionTextView.text = it.weather[0].description
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {

        })
        viewModel.getWeather()
    }
}