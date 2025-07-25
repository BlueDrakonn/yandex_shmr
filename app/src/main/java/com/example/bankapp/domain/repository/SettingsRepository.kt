package com.example.bankapp.domain.repository

interface SettingsRepository {
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)

    fun getPrimaryColor(): Long
    fun setPrimaryColor(color: Long)
}