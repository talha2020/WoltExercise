package com.example.woltexercise

import androidx.lifecycle.*
import com.example.woltexercise.data.ApiError
import com.example.woltexercise.data.UIResponse
import com.example.woltexercise.data.Venues
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository,
                    private val locationLiveData: LocationLiveData): ViewModel() {

    private val venuesMutableLiveData = MutableLiveData<UIResponse<Venues>>()

    fun getVenues(): LiveData<UIResponse<Venues>> {
        return Transformations.switchMap(locationLiveData, ::locationVenueTransformer )
    }

    private fun locationVenueTransformer(location: LocationModel): LiveData<UIResponse<Venues>>{
        viewModelScope.launch {
            venuesMutableLiveData.value = UIResponse.Loading
            val response = mainRepository.getVenues(location.latitude, location.longitude)
            response.error?.also {
                venuesMutableLiveData.value = UIResponse.Error(ApiError(code = it.code, message = it.message))
            }?: response.data?.let {
                //TODO: Have to return first 15 results here
                venuesMutableLiveData.value = UIResponse.Data(it)
            }

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