package com.example.bankapp.features.settings.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import com.example.bankapp.features.settings.models.PinStep

@Composable
fun SetPinScreen(onSuccess: () -> Unit) {
    val pinViewModel: PinViewModel = viewModel(factory = LocalViewModelFactory.current)

    var currentPin by remember { mutableStateOf("") }

    LaunchedEffect(pinViewModel.step) {
        if (pinViewModel.step == PinStep.Success) {
            onSuccess()
        } else if (pinViewModel.step == PinStep.Mismatch) {
            currentPin = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val label = when (pinViewModel.step) {
            PinStep.EnterFirst -> "Введите PIN"
            PinStep.Confirm -> "Повторите PIN"
            PinStep.Mismatch -> "PIN не совпадает. Повторите."
            else -> ""
        }

        Text(label, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        PinInput(currentPin) { digit ->
            if (currentPin.length < 4) {
                currentPin += digit
                if (currentPin.length == 4) {
                    pinViewModel.onPinEntered(currentPin)
                    currentPin = ""
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            currentPin = ""
            pinViewModel.reset()
        }) {
            Text("Сброс")
        }
    }
}

@Composable
fun PinInput(pin: String, onDigit: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(if (index < pin.length) Color.Black else Color.Gray, CircleShape)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Column {
        for (row in listOf("123", "456", "789", "0")) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { digit ->
                    Button(onClick = { onDigit(digit.toString()) }) {
                        Text(digit.toString())
                    }
                }
            }
        }
    }
}
