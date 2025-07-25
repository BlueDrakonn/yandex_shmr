package com.example.bankapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.bankapp.domain.repository.SettingsRepository
import com.example.bankapp.features.settings.repository.PinRepository
import com.example.bankapp.features.settings.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsModule() {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideSettingsRepository(
        prefs: SharedPreferences
    ): SettingsRepository {
        return SettingsRepositoryImpl(prefs)
    }

    @Provides
    @Singleton
    fun providePinRepository(
        context: Context
    ): PinRepository {
        return PinRepository(context)
    }

}