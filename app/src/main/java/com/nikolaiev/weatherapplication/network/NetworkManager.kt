package com.nikolaiev.weatherapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {

    const val API_KEY = "a449d0a6f53a7fa03522a6ec5505f9c6"
    private const val BASE_URL = "https://api.openweathermap.org/"

    var apiService: ApiService


    init {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }

    fun getImageUrl(shortLink: String): String {
        return "http://openweathermap.org/img/wn/${shortLink}.png"
    }
}