package com.nikolaiev.weatherapplication.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.*
import com.nikolaiev.weatherapplication.MainActivity
import com.nikolaiev.weatherapplication.R


class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: MainViewModel
    private var adapter: MainAdapter? = null
    private val REQUEST_PERMISSIONS_CODE = 768
    private var requestLocation: Boolean = true
    private var locationClient: FusedLocationProviderClient? = null
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var weatherListView: RecyclerView
    private val locationsRequest: LocationRequest = LocationRequest()
        .setInterval(10000)
        .setFastestInterval(5000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            viewModel.loading.postValue(false)
            result?.lastLocation?.let {
                viewModel.findCityByLocation(it, context)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = inflater.inflate(R.layout.main_fragment, container, false)
        swipeToRefresh = inflate.findViewById(R.id.swipeToRefresh)
        weatherListView = inflate.findViewById(R.id.weatherListView)
        return inflate
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        activity?.let { locationClient = LocationServices.getFusedLocationProviderClient(it) }
        swipeToRefresh.isEnabled = false
        swipeToRefresh.setOnRefreshListener(this)
        initList()
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            swipeToRefresh.isRefreshing = isLoading
        }
        viewModel.currentCity.observe(viewLifecycleOwner) { city ->
            (activity as? MainActivity)?.supportActionBar?.title = city.name
            requestLocation = false
            stopLocationUpdates()
            onRefresh()
        }
        viewModel.weatherInfo.observe(viewLifecycleOwner) {
            swipeToRefresh.isEnabled = true
            adapter?.setWeatherList(it.daily ?: emptyList())
        }
    }

    private fun initList() {
        weatherListView.layoutManager = LinearLayoutManager(context)
        context?.let {
            adapter = MainAdapter(it)
        }
        weatherListView.adapter = adapter
    }

    override fun onRefresh() {
        viewModel.refreshWeatherList()
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            if (requestLocation) {
                startLocationUpdates()
            }
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_PERMISSIONS_CODE
            )
        }

    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationClient?.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        viewModel.loading.postValue(true)
        locationClient?.requestLocationUpdates(
            locationsRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun checkPermissions(): Boolean {
        val coarseLocation =
            context?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocation = context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return coarseLocation == PackageManager.PERMISSION_GRANTED && fineLocation == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestLocation) {
                    startLocationUpdates()
                }
            }
        }
    }


}