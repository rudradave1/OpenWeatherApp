package com.rudra.weatherinformationapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rudra.weatherinformationapplication.R
import com.rudra.weatherinformationapplication.model.ForecastItem

class ForecastListAdapter() : RecyclerView.Adapter<ForecastListAdapter.MainViewHolder>() {

    var forecasts = mutableListOf<ForecastItem>()

    fun setForecastList(movies: List<ForecastItem>) {
        this.forecasts = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_forecast_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(forecasts[position])
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        fun bind (forecast: ForecastItem) {
            titleTextView.text = forecast.weather[0].main[0].toString()
            descriptionTextView.text = forecast.weather[0].description[0].toString()
        }
    }
}
