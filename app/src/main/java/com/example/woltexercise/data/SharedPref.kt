package com.example.woltexercise.data

interface SharedPref {
    fun saveFirstTimePermissionRequested()
    fun getFirstTimePermissionRequested(): Boolean
    fun addFavorite(id: String)
    fun removeFavorite(id: String)
    fun getFavouritesList(): List<String>
}