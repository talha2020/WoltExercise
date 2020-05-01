package com.example.woltexercise

import androidx.lifecycle.*
import com.example.woltexercise.data.ApiResponse
import com.example.woltexercise.data.Venues
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository,
                    private val locationLiveData: LocationLiveData): ViewModel() {

    private val venuesMutableLiveData = MutableLiveData<ApiResponse<Venues>>()

    fun getVenues(): LiveData<ApiResponse<Venues>> {
        return Transformations.switchMap(locationLiveData, ::locationVenueTransformer )
    }

    private fun locationVenueTransformer(location: LocationModel): LiveData<ApiResponse<Venues>>{
        viewModelScope.launch {
            val venues = mainRepository.getVenues(location.latitude, location.longitude)
            venuesMutableLiveData.value = venues
        }
        return venuesMutableLiveData
    }

    fun getFirstTimePermissionRequested(): Boolean {
        return mainRepository.getFirstTimePermissionRequested()
    }

    fun saveFirstTimePermissionRequested() {
        mainRepository.saveFirstTimePermissionRequested()
    }
}