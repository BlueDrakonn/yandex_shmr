package com.example.bankapp

import android.app.Application
import android.content.Context
import com.example.bankapp.DI.AppComponent
import com.example.bankapp.DI.DaggerAppComponent


class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()


        appComponent = DaggerAppComponent.factory().create(this)
    }

//    companion object {
//        fun get(context: Context): MyApplication {
//            return context.applicationContext as MyApplication
//        }
//    }

}

val Context.appComponent: AppComponent
    get() = when(this) {
        is MyApplication -> this.appComponent
        else -> (this.applicationContext as MyApplication).appComponent
    }
