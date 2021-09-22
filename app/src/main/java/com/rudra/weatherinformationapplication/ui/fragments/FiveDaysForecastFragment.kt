package com.rudra.weatherinformationapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudra.weatherinformationapplication.R
import com.rudra.weatherinformationapplication.model.ForecastItem
import com.rudra.weatherinformationapplication.network.ForecastRepository
import com.rudra.weatherinformationapplication.network.WeatherRepository
import com.rudra.weatherinformationapplication.network.WeatherService
import com.rudra.weatherinformationapplication.ui.adapters.ForecastListAdapter
import com.rudra.weatherinformationapplication.viewmodels.ForecastViewModel
import com.rudra.weatherinformationapplication.viewmodels.ForecastViewModelFactory
import com.rudra.weatherinformationapplication.viewmodels.WeatherViewModel
import com.rudra.weatherinformationapplication.viewmodels.WeatherViewModelFactory

class FiveDaysForecastFragment(private val lat: String, private val lon: String) : Fragment() {

    lateinit var viewModel: ForecastViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ForecastListAdapter

    private val retrofitService = WeatherService.getInstance()
    private val TAG = "FiveDaysForecastFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast_weather, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        val viewModel = ViewModelProvider(this, ForecastViewModelFactory(ForecastRepository(retrofitService, lat, lon, getString(
                R.string.open_weather_api_key)))).get(ForecastViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ForecastListAdapter()
        recyclerView.adapter = adapter

        viewModel.getWeather()

        viewModel.weatherList.observe(requireActivity(), {
            adapter.setForecastList(it)
            Log.d(TAG, "onCreateView: Forecast: $it")
        })


        viewModel.errorMessage.observe(requireActivity(), Observer {
            Log.d(TAG, "onCreateView: $it")
        })

        return view
    }
}