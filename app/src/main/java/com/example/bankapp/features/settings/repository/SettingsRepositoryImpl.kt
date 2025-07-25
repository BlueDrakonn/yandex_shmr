package com.example.bankapp.features.settings.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.bankapp.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val KEY_THEME = "dark_theme"
        private const val KEY_PRIMARY_COLOR = "primary_color"
        private val DEFAULT_PRIMARY_COLOR = 0xFF2AE881
    }

    override fun isDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_THEME, false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_THEME, enabled) }
    }

    override fun getPrimaryColor(): Long {
        return prefs.getLong(KEY_PRIMARY_COLOR, DEFAULT_PRIMARY_COLOR)
    }

    override fun setPrimaryColor(color: Long) {
        prefs.edit { putLong(KEY_PRIMARY_COLOR, color) }
    }
}
