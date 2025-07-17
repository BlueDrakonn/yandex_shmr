package com.example.bankapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ RepositoryModule::class, NetworkModule::class, ViewModelModule::class, DatabaseModule::class
    ]
)
interface AppComponent {

    fun viewModelProviderFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}