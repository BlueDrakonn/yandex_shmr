package com.example.bankapp.features.settings

import androidx.lifecycle.ViewModel
import com.example.bankapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _primaryColor = MutableStateFlow<Int>(settingsRepository.getPrimaryColor())
    val primaryColor = _primaryColor

    private val _isDarkTheme = MutableStateFlow<Boolean>(settingsRepository.isDarkTheme())
    val isDarkTheme = _isDarkTheme

    fun setPrimaryColor(color: Int) {
        settingsRepository.setPrimaryColor(color)
        _primaryColor.value = color
    }

    fun setDarkTheme(enabled: Boolean) {

        settingsRepository.setDarkTheme(enabled)
        _isDarkTheme.value = enabled
    }
    

}