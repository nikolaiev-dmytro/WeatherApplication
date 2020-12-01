package com.nikolaiev.weatherapplication.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nikolaiev.weatherapplication.R
import com.nikolaiev.weatherapplication.model.WeatherData
import com.nikolaiev.weatherapplication.network.NetworkManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainAdapter(private val context: Context) :
    RecyclerView.Adapter<MainAdapter.WeatherInfoHolder>() {

    private val dailyList: MutableList<WeatherData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherInfoHolder {
        return WeatherInfoHolder(
            LayoutInflater.from(context).inflate(R.layout.item_weather_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherInfoHolder, position: Int) {
        holder.bind(dailyList[position])
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

    fun setWeatherList(list: List<WeatherData>) {
        dailyList.clear()
        dailyList.addAll(list)
        notifyDataSetChanged()
    }


    inner class WeatherInfoHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView? = itemView.findViewById(R.id.title)
        private val tvMinTemp: TextView? = itemView.findViewById(R.id.min_temp)
        private val tvMaxTemp: TextView? = itemView.findViewById(R.id.max_temp)
        private val tvShortDescription: TextView? = itemView.findViewById(R.id.weather_short_desc)
        private val ivIcon: ImageView? = itemView.findViewById(R.id.icon)
        fun bind(weatherDataInfo: WeatherData) {
            val simpleDateFormat = SimpleDateFormat("E, d MMM yyyy", Locale.getDefault())
            tvTitle?.text = simpleDateFormat.format(Date((weatherDataInfo.dt?.times(1000) ?: 0)))
            tvMinTemp?.text = itemView.resources.getString(
                R.string.min_temp_celsius,
                weatherDataInfo.temp?.min?.roundToInt()
            )
            tvMaxTemp?.text = itemView.resources.getString(
                R.string.max_temp_celsius,
                weatherDataInfo.temp?.max?.roundToInt()
            )
            if (weatherDataInfo.weather?.isNotEmpty() == true) {
                val weather = weatherDataInfo.weather?.get(0) ?: return
                tvShortDescription?.text = weather.main
                ivIcon?.let {
                    Glide.with(ivIcon)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load(NetworkManager.getImageUrl(weather.icon ?: return))
                        .into(ivIcon)
                }

            }


        }

    }

}