package com.example.bankapp

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: MyApplication

        val context: Context by lazy {
            instance.applicationContext
        }
    }
}