package com.example.woltexercise.venues

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import com.example.woltexercise.BuildConfig
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<LocationModel>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0

    private val updateLocationTask = object : Runnable {
        override fun run() {
            value = locationList[currentIndex]
            currentIndex = (currentIndex + 1).rem(locationList.size)
            handler.postDelayed(this, 10000)
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (BuildConfig.DEBUG){
            handler.removeCallbacks(updateLocationTask)
        } else{
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        if (BuildConfig.DEBUG){
            handler.post(updateLocationTask)
        } else{
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private fun setLocationData(location: Location) {
        value = LocationModel(
            longitude = location.longitude,
            latitude = location.latitude
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}

data class LocationModel(
    val latitude: Double,
    val longitude: Double
)

private val locationList = listOf(
    LocationModel(60.170187, 24.930599),
    LocationModel(60.169418, 24.931618),
    LocationModel(60.169818, 24.932906),
    LocationModel(60.170005, 24.935105),
    LocationModel(60.169108, 24.936210),
    LocationModel(60.168355, 24.934869),
    LocationModel(60.167560, 24.932562),
    LocationModel(60.168254, 24.931532),
    LocationModel(60.169012, 24.930341),
    LocationModel(60.170085, 24.929569)
)