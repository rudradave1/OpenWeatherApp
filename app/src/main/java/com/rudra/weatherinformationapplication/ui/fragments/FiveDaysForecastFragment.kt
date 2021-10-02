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
import com.rudra.weatherinformationapplication.data.model.ForecastItem
import com.rudra.weatherinformationapplication.network.ForecastRepository
import com.rudra.weatherinformationapplication.network.WeatherService
import com.rudra.weatherinformationapplication.ui.adapters.ForecastListAdapter
import com.rudra.weatherinformationapplication.viewmodels.ForecastViewModel
import com.rudra.weatherinformationapplication.viewmodels.ForecastViewModelFactory

class FiveDaysForecastFragment(private val lat: String, private val lon: String) : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ForecastListAdapter
    private var forecastList: List<ForecastItem> = arrayListOf()

    private val retrofitService = WeatherService.getInstance()
    private val TAG = "FiveDaysForecastFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast_weather, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ForecastListAdapter()
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this, ForecastViewModelFactory(ForecastRepository(retrofitService, lat, lon, getString(
            R.string.open_weather_api_key)))).get(ForecastViewModel::class.java)

        viewModel.getForecast()

        val forecastList = mutableListOf<ForecastItem>()

        viewModel.weatherList.observe(requireActivity(), { it ->
            if (it != null) {
                for (i in it.list.indices step (8)) {
                    forecastList.add(it.list[i])
                }

                adapter.setForecastList(forecastList)
                Log.d(TAG, "onCreateView: Forecast: $it")
            }
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            Log.d(TAG, "onCreateView: $it")
        })

    }
}