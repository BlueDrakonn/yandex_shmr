package com.example.bankapp.features.settings.repository

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.example.bankapp.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val KEY_THEME = "dark_theme"
        private const val KEY_PRIMARY_COLOR = "primary_color"
        private val DEFAULT_PRIMARY_COLOR = Color(0xFF2AE881).value.toInt()
    }

    override fun isDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_THEME, false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_THEME, enabled) }
    }

    override fun getPrimaryColor(): Int {
        return prefs.getInt(KEY_PRIMARY_COLOR, DEFAULT_PRIMARY_COLOR)
    }

    override fun setPrimaryColor(color: Int) {
        prefs.edit { putInt(KEY_PRIMARY_COLOR, color) }
    }
}
