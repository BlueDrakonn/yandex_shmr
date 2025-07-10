package com.example.bankapp.features.transactionAction.edit

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.di.LocalViewModelFactory

@Composable
fun TransactionEditScreen(

    navController: NavHostController,
    type: Boolean
) {
    val viewModel: TransactionEditViewModel = viewModel(factory = LocalViewModelFactory.current)


}