package com.example.bankapp.features.settings

import ListItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.settings.utils.SettingsItems
import com.example.bankapp.navigation.TopAppBar


@Composable
fun SettingsScreen() {
    val settingsItems = SettingsItems.items


    val settingsViewModel: SettingsViewModel = viewModel(factory = LocalViewModelFactory.current)
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Screen.SETTINGS.titleRes),
            )
        }

    ) { padding ->
        LazyList(
            itemsList = settingsItems,
            itemTemplate = { item ->

                val clickableModifier = when (item) {
                    R.string.settings_dark_theme -> Modifier
                    else -> Modifier.clickable { }
                }

                ListItem(
                    modifier = clickableModifier
                        .height(55.dp),
                    content = {
                        Text(
                            text = stringResource(item),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    trailingContent = {

                        if (item == R.string.settings_dark_theme) {

                            Box {

                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = {
                                        settingsViewModel.setDarkTheme(!isDarkTheme)

                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                                        checkedTrackColor = MaterialTheme.colorScheme.secondary,
                                        checkedBorderColor = MaterialTheme.colorScheme.primary

                                    )
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.arrow_right),
                                    contentDescription = null,
                                )
                            }
                        }

                    }
                )
            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )
    }


}

