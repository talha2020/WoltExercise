package com.example.woltexercise.data

interface SharedPref {
    fun saveFirstTimePermissionRequested()
    fun getFirstTimePermissionRequested(): Boolean
}