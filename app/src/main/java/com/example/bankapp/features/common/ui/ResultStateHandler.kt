package com.example.bankapp.features.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.bankapp.core.ResultState

@Composable
fun <T> ResultStateHandler(
    state: ResultState<T>,
    onSuccess: @Composable (data: T) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is ResultState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ResultState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                onSuccess(state.data)
            }

        }
        is ResultState.Error -> {
            Box(modifier = modifier.fillMaxSize()) {
                ErrorScreen(
                    errorMessage = state.message,
                    errorCode = state.code
                )
            }
        }
    }
}
