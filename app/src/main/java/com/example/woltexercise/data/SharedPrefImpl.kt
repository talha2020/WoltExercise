package com.example.woltexercise.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefImpl (context: Context): SharedPref {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    override fun saveFirstTimePermissionRequested() {
        preferences.edit().putBoolean("firstTime", false).apply()
    }

    override fun getFirstTimePermissionRequested(): Boolean {
        return preferences.getBoolean("firstTime", true)
    }

    override fun addFavorite(id: String){
        val list = getFavouritesList() as MutableList
        list.add(id)
        saveFavorites(list)
    }

    override fun removeFavorite(id: String){
        val list = getFavouritesList() as MutableList
        list.remove(id)
        saveFavorites(list)
    }

    // The list of favorites can grow over time so it is suitable to save them in a DB rather than preferences.
    // Used preferences to keep it simple here.
    private fun saveFavorites(favoritesList: List<String>){
        preferences.edit().putString("favorites", Gson().toJson(favoritesList)).apply()
    }

    override fun getFavouritesList(): List<String>{
        val typeToken = object : TypeToken<List<String>>() {}.type
        val favoritesString = preferences.getString("favorites", null)
        return if (favoritesString != null)
            Gson().fromJson(favoritesString, typeToken)
        else mutableListOf()
    }
}