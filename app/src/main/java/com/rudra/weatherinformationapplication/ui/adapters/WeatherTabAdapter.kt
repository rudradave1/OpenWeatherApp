package com.rudra.weatherinformationapplication.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rudra.weatherinformationapplication.ui.fragments.CurrentWeatherFragment
import com.rudra.weatherinformationapplication.ui.fragments.FiveDaysForecastFragment

class WeatherTabAdapter(private val myContext: Context, fm: FragmentManager, private var totalTabs: Int,
                        private val lat: String, private val lon: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return CurrentWeatherFragment(lat, lon)
            }
            1 -> {
                return FiveDaysForecastFragment(lat, lon)
            }
            else -> return CurrentWeatherFragment(lat, lon)
        }
    }

        // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
