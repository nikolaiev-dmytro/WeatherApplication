package com.nikolaiev.weatherapplication.ui.main

import android.content.Context
import android.content.res.AssetManager
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikolaiev.weatherapplication.model.DataResponse
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nikolaiev.weatherapplication.R
import com.nikolaiev.weatherapplication.model.CityModel
import com.nikolaiev.weatherapplication.model.WeatherInfo
import com.nikolaiev.weatherapplication.network.NetworkManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private var weatherObserver: DisposableSingleObserver<WeatherInfo>? = null
    private var findCityObserver: DisposableSingleObserver<DataResponse>? = null
    private val _weatherInfo: MutableLiveData<WeatherInfo> by lazy { MutableLiveData() }
    private val _currentCity: MutableLiveData<CityModel> by lazy { MutableLiveData() }
    val currentCity: LiveData<CityModel> = _currentCity
    val weatherInfo: LiveData<WeatherInfo> = _weatherInfo
    val loading: MutableLiveData<Boolean> by lazy { MutableLiveData() }


    fun refreshWeatherList() {
        getWeatherByCoordinates(currentCity.value?.coord?.lat, currentCity.value?.coord?.lon)
    }

    private fun getWeatherByCoordinates(lat: Double?, lon: Double?) {
        loading.postValue(true)
        weatherObserver = object : DisposableSingleObserver<WeatherInfo>() {
            override fun onSuccess(t: WeatherInfo) {
                _weatherInfo.postValue(t)
                loading.postValue(false)

            }

            override fun onError(e: Throwable) {
                loading.postValue(false)

            }

        }
        NetworkManager.apiService.getDailyWeather(lat, lon)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(weatherObserver!!)
    }


    override fun onCleared() {
        findCityObserver?.dispose()
        weatherObserver?.dispose()
        weatherObserver = null
        findCityObserver = null
        super.onCleared()
    }

    fun findCityByLocation(location: Location, context: Context?) {
        loading.postValue(true)

        findCityObserver = object : DisposableSingleObserver<DataResponse>() {
            override fun onSuccess(t: DataResponse) {
                t.list?.filter { it.coord != null }?.minByOrNull {
                    val currentLocation = Location("")
                    currentLocation.latitude = it.coord?.lat ?: 0.0
                    currentLocation.longitude = it.coord?.lon ?: 0.0
                    location.distanceTo(currentLocation)
                }?.let {
                    _currentCity.postValue(it)
                }
                loading.postValue(false)
            }

            override fun onError(e: Throwable) {
                loading.postValue(false)

            }
        }
        NetworkManager.apiService.getCitiesInCircle(location.latitude, location.longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(findCityObserver!!)

    }
}