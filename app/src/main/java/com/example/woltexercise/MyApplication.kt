package com.example.woltexercise

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        private lateinit var mContext: MyApplication

        fun getContext(): MyApplication {
            return mContext
        }
    }
}