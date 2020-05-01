package com.example.woltexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun getVenues(){
        viewModelScope.launch {
            val venues = mainRepository.getVenues(60.170187, 24.930599)
        }
    }
}