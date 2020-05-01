package com.example.woltexercise.data

sealed class UIResponse<out T> {
    data class Error(val error: ApiError) : UIResponse<Nothing>()
    data class Data<T>(val data: T) : UIResponse<T>()
    object Loading : UIResponse<Nothing>()
}