package com.example.bankapp.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bankapp.R
import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.features.common.AddButton
import com.example.bankapp.features.account.AccountsScreen
import com.example.bankapp.features.categories.CategoriesScreen
import com.example.bankapp.features.expenses.ExpensesScreen
import com.example.bankapp.features.history.HistoryScreen
import com.example.bankapp.features.income.IncomeScreen
import com.example.bankapp.features.settings.SettingsScreen
import com.example.bankapp.features.history.TransactionType


const val TYPE_ARG = "type"


enum class Screen(
    val route: String,
    val titleRes: Int,
    val buttonTitleRes: Int? = null,
    val iconId: Int? = null,
) {
    EXPENSES(
        "EXPENSES",
        R.string.expenses_title,
        R.string.expenses_button,
        R.drawable.expenses),
    INCOME(
        "INCOME",
        R.string.income_title,
        R.string.income_button,
        R.drawable.income),
    ACCOUNTS(
        "ACCOUNTS",
        R.string.accounts_title,
        R.string.accounts_button,
        R.drawable.accounts),
    ARTICLES(
        "ARTICLES",
        R.string.articles_title,
        R.string.articles_button,
        R.drawable.articles),
    SETTINGS(
        "SETTINGS",
        R.string.settings_title,
        R.string.settings_button,
        R.drawable.settings),
    HISTORY_EXPENSES(
        "HISTORY_EXPENSES",
        R.string.history_title,
    ),
    HISTORY_INCOME(
        "HISTORY_INCOME",
        R.string.history_title,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val currentScreen = Screen.valueOf(
        navController.currentBackStackEntryAsState().value?.destination?.route ?: "EXPENSES"
    )

    val  mainViewModel: MainViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController) },
        topBar = {

            TopAppBar(
                navController = navController,
                currentScreen = currentScreen
            )

        },
        floatingActionButton = {when (currentScreen) {
            Screen.EXPENSES -> AddButton(onClick = {})
            Screen.INCOME -> AddButton(onClick = {})
            Screen.ACCOUNTS -> AddButton(onClick = {})
            else -> Unit
        }}
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.EXPENSES.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.EXPENSES.route) { ExpensesScreen(mainViewModel) }
            composable(Screen.INCOME.route) { IncomeScreen(mainViewModel) }
            composable(Screen.ACCOUNTS.route) { AccountsScreen(mainViewModel) }
            composable(Screen.ARTICLES.route) { CategoriesScreen(mainViewModel) }
            composable(Screen.SETTINGS.route) { SettingsScreen() }
            composable(Screen.HISTORY_INCOME.route) {
                HistoryScreen(type = TransactionType.INCOME,viewModel = mainViewModel)
            }
            composable(Screen.HISTORY_EXPENSES.route) {
                HistoryScreen(type = TransactionType.EXPENSE,viewModel = mainViewModel)
            }
        }
    }
}












