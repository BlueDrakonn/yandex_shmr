package com.example.bankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.navigation.AppNavigation
import com.example.bankapp.ui.theme.BankAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        enableEdgeToEdge()

        setContent {
            BankAppTheme {
                CompositionLocalProvider(LocalViewModelFactory provides appComponent.viewModelProviderFactory()) {
                    AppNavigation()
                }
            }
        }
    }
}



