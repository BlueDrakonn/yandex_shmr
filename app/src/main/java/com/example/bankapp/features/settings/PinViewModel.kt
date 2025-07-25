package com.example.bankapp.features.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bankapp.features.settings.models.PinStep
import com.example.bankapp.features.settings.repository.PinRepository
import javax.inject.Inject

class PinViewModel @Inject constructor(private val pinRepository: PinRepository) : ViewModel() {

    var step by mutableStateOf(PinStep.EnterFirst)
        private set

    var firstEntry by mutableStateOf("")
        private set

    fun hasPin(): Boolean = pinRepository.hasPin()

    fun savePin(pin: String) {
        pinRepository.savePin(pin)
    }

    fun verifyPin(pin: String): Boolean {
        return pinRepository.verifyPin(pin)
    }

    fun onPinEntered(pin: String) {
        when (step) {
            PinStep.EnterFirst -> {
                firstEntry = pin
                step = PinStep.Confirm
            }
            PinStep.Confirm -> {
                if (pin == firstEntry) {
                    savePin(pin)
                    step = PinStep.Success
                } else {
                    step = PinStep.Mismatch
                }
            }
            else -> {}
        }
    }

    fun reset() {
        step = PinStep.EnterFirst
        firstEntry = ""
    }
}


