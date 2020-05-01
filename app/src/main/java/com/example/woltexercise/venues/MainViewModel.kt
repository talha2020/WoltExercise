package com.example.woltexercise.venues

import androidx.lifecycle.*
import com.example.woltexercise.data.ApiError
import com.example.woltexercise.data.Place
import com.example.woltexercise.data.UIResponse
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository,
                    private val locationLiveData: LocationLiveData
): ViewModel() {

    //private val venuesMutableLiveData = MutableLiveData<UIResponse<List<Place>>>()

    fun getVenues(): LiveData<UIResponse<List<Place>>> {
        return Transformations.switchMap(locationLiveData, ::locationVenueTransformer )
    }

    private fun locationVenueTransformer(location: LocationModel): LiveData<UIResponse<List<Place>>>{
        val venuesMutableLiveData = MutableLiveData<UIResponse<List<Place>>>()
        viewModelScope.launch {
            venuesMutableLiveData.value = UIResponse.Loading
            val response = mainRepository.getVenues(location.latitude, location.longitude)
            response.error?.also {
                venuesMutableLiveData.value = UIResponse.Error(ApiError(code = it.code, message = it.message))
            }?: response.data?.let {
                venuesMutableLiveData.value = UIResponse.Data(it.places.take(15))
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

    fun setFavorite(place: Place) {
        mainRepository.setFavorite(place)
    }
}