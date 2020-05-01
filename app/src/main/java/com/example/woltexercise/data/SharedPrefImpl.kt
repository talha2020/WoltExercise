package com.example.woltexercise.data

import android.content.Context
import android.content.SharedPreferences

class SharedPrefImpl (context: Context): SharedPref {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    override fun saveFirstTimePermissionRequested() {
        preferences.edit().putBoolean("firstTime", false).apply()
    }

    override fun getFirstTimePermissionRequested(): Boolean {
        return preferences.getBoolean("firstTime", true)
    }
}