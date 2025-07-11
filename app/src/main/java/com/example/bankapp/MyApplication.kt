package com.example.bankapp

import android.app.Application
import android.content.Context
import com.example.bankapp.di.AppComponent
import com.example.bankapp.di.DaggerAppComponent


class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }


}
val Context.appComponent: AppComponent
    get() = when(this) {
        is MyApplication -> this.appComponent
        else -> (this.applicationContext as MyApplication).appComponent
    }