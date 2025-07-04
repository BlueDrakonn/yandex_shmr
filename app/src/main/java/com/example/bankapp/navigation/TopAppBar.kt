package com.example.bankapp.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.example.bankapp.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navigationIcon: @Composable () -> Unit = {},
    title: String = "",
    action: @Composable () -> Unit = {},
){
    CenterAlignedTopAppBar(
        navigationIcon = {
            navigationIcon()
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryGreen,
        ),

        actions = {
            action()
            }
    )

}