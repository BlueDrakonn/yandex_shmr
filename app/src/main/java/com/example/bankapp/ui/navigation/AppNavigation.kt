package com.example.bankapp.ui.navigation


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bankapp.R
import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.ui.common.AddButton
import com.example.bankapp.ui.screen.AccountsScreen
import com.example.bankapp.ui.screen.CategoriesScreen
import com.example.bankapp.ui.screen.ExpensesScreen
import com.example.bankapp.ui.screen.HistoryScreen
import com.example.bankapp.ui.screen.IncomeScreen
import com.example.bankapp.ui.screen.SettingsScreen
import com.example.bankapp.ui.screen.TransactionType
import com.example.bankapp.ui.theme.PrimaryGreen
import java.util.Locale


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












