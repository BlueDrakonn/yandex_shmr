package com.example.bankapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.settings.SettingsViewModel
import com.example.bankapp.navigation.AppNavigation
import com.example.bankapp.ui.theme.BankAppTheme
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        installSplashScreen()

        enableEdgeToEdge()


        setContent {
            settingsViewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            val primaryColor by settingsViewModel.primaryColor.collectAsState()
            CompositionLocalProvider(LocalViewModelFactory provides appComponent.viewModelProviderFactory()) {

                BankAppTheme(isDarkTheme, primaryColor) {

                    Log.d("RECOMPOSE", "")
                    AppNavigation()
                }
            }
        }
    }
}



