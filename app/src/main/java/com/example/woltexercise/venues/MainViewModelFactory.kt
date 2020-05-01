package com.example.woltexercise.venues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.woltexercise.MyApplication
import com.example.woltexercise.api.ApiClient
import com.example.woltexercise.data.SharedPrefImpl

class MainViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val preferences = SharedPrefImpl(MyApplication.getContext())
            return MainViewModel(
                mainRepository = MainRepository(
                    ApiClient(),
                    preferences
                ),
                locationLiveData = LocationLiveData(MyApplication.getContext())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}