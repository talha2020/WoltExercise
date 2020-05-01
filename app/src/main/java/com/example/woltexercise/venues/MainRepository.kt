package com.example.woltexercise.venues

import com.example.woltexercise.api.ApiClient
import com.example.woltexercise.data.ApiResponse
import com.example.woltexercise.data.Place
import com.example.woltexercise.data.SharedPref
import com.example.woltexercise.data.Venues

class MainRepository(private val apiClient: ApiClient,
                     private val sharedPref: SharedPref) {

    suspend fun getVenues(lat: Double, lng: Double): ApiResponse<Venues> {
        val venues = apiClient.getVenues(lat, lng)
        val favoritesIdList = sharedPref.getFavouritesList()
        venues.data?.places?.apply { map { it.favourite = favoritesIdList.contains(it.id.oid)  } }
        return venues
    }

    fun saveFirstTimePermissionRequested() {
        sharedPref.saveFirstTimePermissionRequested()
    }

    fun getFirstTimePermissionRequested(): Boolean {
        return sharedPref.getFirstTimePermissionRequested()
    }

    fun setFavorite(place: Place) {
        if (place.favourite)
            sharedPref.addFavorite(place.id.oid)
        else
            sharedPref.removeFavorite(place.id.oid)
    }
}