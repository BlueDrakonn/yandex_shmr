package com.example.bankapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController,
    currentScreen: Screen
){
    CenterAlignedTopAppBar(
        navigationIcon = {
            when(currentScreen) {
                Screen.HISTORY_INCOME -> IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "navigate to previous screen")
                }
                Screen.HISTORY_EXPENSES -> IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "navigate to previous screen")
                }
                else -> Unit
            }
        },
        title = {
            Text(
                text = stringResource(currentScreen.titleRes),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryGreen,
        ),

        actions = {
            when (currentScreen) {
                Screen.EXPENSES -> IconButton(
                    onClick = { navController.navigate(Screen.HISTORY_EXPENSES.route)}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.history),
                        contentDescription = "history"
                    ) }
                Screen.INCOME -> IconButton(
                    onClick = {navController.navigate(Screen.HISTORY_INCOME.route)}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.history),
                        contentDescription = "history"
                    )
                }
                Screen.ACCOUNTS -> IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = "edit"
                    )
                }
                Screen.HISTORY_INCOME -> IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.state_layer),
                        contentDescription = "change history period"
                    )
                }
                Screen.HISTORY_EXPENSES -> IconButton(
                    onClick = {
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.state_layer),
                        contentDescription = "change history period"
                    )
                }
                else -> Unit
            }

        }
    )

}