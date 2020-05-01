package com.example.woltexercise

import com.example.woltexercise.api.ApiClient
import com.example.woltexercise.data.ApiResponse
import com.example.woltexercise.data.Venues

class MainRepository(private val apiClient: ApiClient) {

    suspend fun getVenues(lat: Double, lng: Double): ApiResponse<Venues> {
        return apiClient.getVenues(lat, lng)
    }
}