package com.example.bankapp.data.local

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class FirstLaunchManager @Inject constructor(
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean = prefs.getBoolean("is_first_launch", true)

    fun markAppLaunched() {
        prefs.edit { putBoolean("is_first_launch", false) }
    }
}
