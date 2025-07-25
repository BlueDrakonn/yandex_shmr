package com.example.bankapp.features.settings.repository

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PinRepository(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_PIN = "user_pin"
    }

    fun savePin(pin: String) {
        prefs.edit { putString(KEY_PIN, pin) }
    }

    fun getSavedPin(): String? = prefs.getString(KEY_PIN, null)

    fun hasPin(): Boolean = prefs.contains(KEY_PIN)

    fun verifyPin(input: String): Boolean = input == getSavedPin()
}
