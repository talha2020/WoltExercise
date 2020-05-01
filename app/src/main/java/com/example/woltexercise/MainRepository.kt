package com.example.woltexercise

import com.example.woltexercise.api.ApiClient
import com.example.woltexercise.data.ApiResponse
import com.example.woltexercise.data.SharedPref
import com.example.woltexercise.data.Venues

class MainRepository(private val apiClient: ApiClient,
                     private val sharedPref: SharedPref) {

    suspend fun getVenues(lat: Double, lng: Double): ApiResponse<Venues> {
        return apiClient.getVenues(lat, lng)
    }

    fun saveFirstTimePermissionRequested() {
        sharedPref.saveFirstTimePermissionRequested()
    }

    fun getFirstTimePermissionRequested(): Boolean {
        return sharedPref.getFirstTimePermissionRequested()
    }
}