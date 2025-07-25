package com.example.bankapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkerFactory
import com.example.bankapp.MainActivity
import com.example.bankapp.di.local.DatabaseModule
import com.example.bankapp.di.local.LocalRepositoryModule
import com.example.bankapp.di.remote.NetworkModule
import com.example.bankapp.di.remote.RemoteRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RemoteRepositoryModule::class, LocalRepositoryModule::class, RepositoryModule::class, NetworkModule::class, ViewModelModule::class, DatabaseModule::class, WorkerModule::class, SettingsModule::class
    ]
)
interface AppComponent {

    fun workerFactory(): WorkerFactory

    fun viewModelProviderFactory(): ViewModelProvider.Factory
    fun inject(mainActivity: MainActivity)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}