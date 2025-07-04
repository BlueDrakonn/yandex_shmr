package com.example.bankapp.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bankapp.core.navigation.HistoryType
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.features.account.AccountEditScreen
import com.example.bankapp.features.account.AccountsScreen
import com.example.bankapp.features.categories.CategoriesScreen
import com.example.bankapp.features.common.ui.AddButton
import com.example.bankapp.features.expenses.ExpensesScreen
import com.example.bankapp.features.history.HistoryScreen
import com.example.bankapp.features.income.IncomeScreen
import com.example.bankapp.features.settings.SettingsScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val currentScreen = Screen.valueOf(
        navController.currentBackStackEntryAsState().value?.destination?.route ?: "EXPENSES"
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {

        },
        floatingActionButton = {
            when (currentScreen) {
                Screen.EXPENSES -> AddButton(onClick = {})
                Screen.INCOME -> AddButton(onClick = {})
                else -> Unit
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.EXPENSES.route,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable(Screen.EXPENSES.route) {
                ExpensesScreen(navController = navController)
            }
            composable(Screen.INCOME.route) {
                IncomeScreen(navController = navController)
            }
            composable(Screen.ACCOUNTS.route) {
                AccountsScreen(navController = navController)
            }
            composable(Screen.ARTICLES.route) {
                CategoriesScreen()
            }
            composable(Screen.SETTINGS.route) {
                SettingsScreen()
            }
            composable(Screen.HISTORY_INCOME.route) {
                HistoryScreen(type = HistoryType.INCOME, navController = navController)
            }
            composable(Screen.HISTORY_EXPENSES.route) {
                HistoryScreen(type = HistoryType.EXPENSE, navController = navController)
            }
            composable(Screen.ACCOUNTS_EDIT.route) {
                AccountEditScreen(navController = navController)
            }
        }
    }
}












