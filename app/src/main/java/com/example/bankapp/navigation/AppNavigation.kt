package com.example.bankapp.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.core.navigation.TransactionType
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.account.account.AccountsScreen
import com.example.bankapp.features.account.accountEdit.AccountEditScreen
import com.example.bankapp.features.analysis.AnalysisScreen
import com.example.bankapp.features.categories.CategoriesScreen
import com.example.bankapp.features.expenses.ExpensesScreen
import com.example.bankapp.features.firstLaunch.MainViewModel
import com.example.bankapp.features.history.HistoryScreen
import com.example.bankapp.features.income.IncomeScreen
import com.example.bankapp.features.settings.PinViewModel
import com.example.bankapp.features.settings.screens.EnterPinScreen
import com.example.bankapp.features.settings.screens.PrimaryColorScreen
import com.example.bankapp.features.settings.screens.SetPinScreen
import com.example.bankapp.features.settings.screens.SettingsScreen
import com.example.bankapp.features.transactionAction.add.TransactionAddScreen
import com.example.bankapp.features.transactionAction.edit.TransactionEditScreen


@Composable
fun AppNavigation() {
    val viewModel: MainViewModel = viewModel(factory = LocalViewModelFactory.current)
    val pinViewModel: PinViewModel = viewModel(factory = LocalViewModelFactory.current)
    viewModel.start()
    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {

        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = if (pinViewModel.hasPin()) Screen.ENTER_PIN.route else Screen.EXPENSES.route,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable(Screen.ENTER_PIN.route) {
                EnterPinScreen { navController.navigate(Screen.EXPENSES.route) }
            }

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
                SettingsScreen(navController = navController)
            }
            composable(Screen.CHOOSE_PRIMARY_COLOR.route) {
                PrimaryColorScreen()
            }
            composable(Screen.PIN.route) {
                SetPinScreen { navController.popBackStack() }
            }

            composable(Screen.HISTORY_INCOME.route) {
                HistoryScreen(type = TransactionType.INCOME, navController = navController)
            }
            composable(Screen.HISTORY_EXPENSES.route) {
                HistoryScreen(type = TransactionType.EXPENSE, navController = navController)
            }
            composable(Screen.ACCOUNTS_EDIT.route) {
                AccountEditScreen(navController = navController)
            }

            composable(
                "${Screen.TRANSACTION_ADD.route}?type={type}",
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.BoolType
                        defaultValue = false
                    })
            ) { backStackEntry ->


                TransactionAddScreen(
                    navController = navController,
                    type = backStackEntry.arguments?.getBoolean("type") ?: false
                )

            }

            composable(
                "${Screen.TRANSACTION_EDIT.route}?type={type}?transactionId={transactionId}",
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.BoolType
                        defaultValue = false
                    },
                    navArgument("transactionId") {
                        type = NavType.IntType
                        defaultValue = 1
                    })
            ) { backStackEntry ->

                TransactionEditScreen(
                    navController = navController,
                    type = backStackEntry.arguments?.getBoolean("type") ?: false,
                    transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 1
                )
            }

            composable(
                "${Screen.ANALYSIS.route}?type={type}",
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.BoolType
                        defaultValue = false
                    })
            ) { backStackEntry ->


                AnalysisScreen(
                    navController = navController,
                    isIncomeTransactions = backStackEntry.arguments?.getBoolean("type") ?: false
                )

            }


        }
    }
}












