package com.example.woltexercise

import com.example.woltexercise.api.ApiClient

class MainRepository(private val apiClient: ApiClient) {

    suspend fun getVenues(lat: Double, lng: Double){
        apiClient.getVenues(lat, lng)
    }
}