package com.example.woltexercise.data

data class ApiError(
    val code: String? = "",
    val message: String? = "Unknown error occurred"
)