package com.example.bankapp.features.settings.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.settings.PinViewModel

@Composable
fun EnterPinScreen(onSuccess: () -> Unit) {
    val pinViewModel: PinViewModel = viewModel(factory = LocalViewModelFactory.current)
    var enteredPin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }


    LaunchedEffect(enteredPin.length) {

        if (enteredPin.length == 4) {

            if (pinViewModel.verifyPin(enteredPin)) {
                Log.d("PIN_RER", enteredPin)
                onSuccess()
            } else {
                error = true
                enteredPin = ""
            }
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Введите PIN", style = MaterialTheme.typography.titleLarge)
        if (error) Text("Неверный PIN", color = Color.Red)
        Spacer(Modifier.height(16.dp))
        PinInput(enteredPin) { digit ->
            if (enteredPin.length < 4) {
                enteredPin += digit
            }
        }
    }
}
