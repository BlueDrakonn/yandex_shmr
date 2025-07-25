package com.example.bankapp.features.settings.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankapp.R
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.settings.SettingsViewModel
import com.example.bankapp.navigation.TopAppBar

@Composable
fun PrimaryColorScreen() {
    val settingsViewModel: SettingsViewModel = viewModel(factory = LocalViewModelFactory.current)

    val currentColor by settingsViewModel.primaryColor.collectAsStateWithLifecycle()
    val colors = listOf(
        0xFF00C853.toInt(),
        0xFFFF5722.toInt(),
        0xFF3F51B5.toInt(),
        0xFFFFC107.toInt(),
        0xFFE91E63.toInt(),
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.choose_primary_color_ru),
            )
        }

    ) { padding ->
        Column(modifier = Modifier.padding(top = padding.calculateTopPadding())) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                colors.forEach { colorInt ->
                    val color = Color(colorInt)
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (colorInt == currentColor) 3.dp else 1.dp,
                                color = if (colorInt == currentColor) Color.Black else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable {
                                settingsViewModel.setPrimaryColor(colorInt)
                            }
                    )
                }
            }

        }
    }


}
