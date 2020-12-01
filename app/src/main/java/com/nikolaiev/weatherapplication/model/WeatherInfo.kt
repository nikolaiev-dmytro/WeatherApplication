package com.nikolaiev.weatherapplication.model
import com.google.gson.annotations.SerializedName

data class WeatherInfo(
    @SerializedName("daily")
    var daily: List<WeatherData>?,
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lon")
    var lon: Double?,
    @SerializedName("timezone")
    var timezone: String?,
    @SerializedName("timezone_offset")
    var timezoneOffset: Int?
)