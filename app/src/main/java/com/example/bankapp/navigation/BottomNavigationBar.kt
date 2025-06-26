package com.example.bankapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.EXPENSES,
        Screen.INCOME,
        Screen.ACCOUNTS,
        Screen.ARTICLES,
        Screen.SETTINGS
    )

    BottomAppBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        val selectedItem = items.find { it.route == currentRoute }?.route ?: navController.previousBackStackEntry?.destination?.route

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            items.forEach { screen ->

                screen.iconId?.let {
                    screen.buttonTitleRes?.let { it1 ->
                        BottomNavigationItem(
                            iconId = it,
                            buttonTitleRes = it1,
                            selected = selectedItem == screen.route,
                            onClick = { navController.navigate(screen.route) }
                        )
                    }
                }
            }

        }

    }
}