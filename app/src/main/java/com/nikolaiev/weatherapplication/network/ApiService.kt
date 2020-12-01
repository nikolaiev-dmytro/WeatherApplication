package com.nikolaiev.weatherapplication.network

import com.google.gson.JsonObject
import com.nikolaiev.weatherapplication.model.DataResponse
import com.nikolaiev.weatherapplication.model.WeatherInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("data/2.5/onecall?exclude=current,minutely,hourly,alerts&appid=${NetworkManager.API_KEY}&units=metric")
    fun getDailyWeather(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
    ): Single<WeatherInfo>

    @GET("data/2.5/find?cnt=10&appid=${NetworkManager.API_KEY}&units=metric")
    fun getCitiesInCircle(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
    ): Single<DataResponse>

    @GET("data/2.5/onecall?exclude=minutely,daily,alerts&appid=${NetworkManager.API_KEY}&units=metric")
    fun getHourlyWeather(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
    ): Single<WeatherInfo>
}