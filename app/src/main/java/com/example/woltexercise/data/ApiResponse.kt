package com.example.woltexercise.data

data class ApiResponse<T> (
    val data: T? = null,
    val error: ApiError? = null
)