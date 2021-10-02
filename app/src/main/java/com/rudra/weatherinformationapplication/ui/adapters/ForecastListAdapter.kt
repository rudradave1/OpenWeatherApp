package com.rudra.weatherinformationapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rudra.weatherinformationapplication.R
import com.rudra.weatherinformationapplication.data.model.ForecastItem
import com.rudra.weatherinformationapplication.data.model.WeatherXX

class ForecastListAdapter() : RecyclerView.Adapter<ForecastListAdapter.MainViewHolder>() {

    private var forecasts = mutableListOf<ForecastItem>()

    fun setForecastList(movies: List<ForecastItem>) {
        this.forecasts = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_forecast_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(forecasts[position].weather[0], forecasts[position].dt_txt.toString())
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        fun bind (forecast: WeatherXX, date: String) {
            titleTextView.text = forecast.main
            descriptionTextView.text = forecast.description
            dateTextView.text = date
        }
    }
}
