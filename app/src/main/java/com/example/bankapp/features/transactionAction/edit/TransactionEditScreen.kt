package com.example.bankapp.features.transactionAction.edit

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.account.accountEdit.showToast
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.transactionAction.add.TransactionForm
import com.example.bankapp.features.transactionAction.models.RequestState
import com.example.bankapp.features.transactionAction.ui.ErrorDialog
import com.example.bankapp.navigation.TopAppBar

@Composable
fun TransactionEditScreen(
    navController: NavHostController,
    type: Boolean,
    transactionId: Int
) {
    val viewModel: TransactionEditViewModel = viewModel(factory = LocalViewModelFactory.current)

    val context = LocalContext.current
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()


    when (requestState) {
        is RequestState.Error -> {

            val errorMessage = (requestState as RequestState.Error).message
            Log.d("ERROR_TRANS", errorMessage)
            ErrorDialog(
                message = errorMessage,
                onRetry = {
                    viewModel.editTransaction(transactionId = transactionId)
                },
                onCancel = {
                    viewModel.requestDialogDismiss()
                },
            )
        }

        RequestState.Idle -> {

        }
        RequestState.Loading -> {

            Popup(alignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }

        RequestState.Success -> {
            showToast(context, context.getString(R.string.transaction_edited_successful))
            navController.popBackStack()
        }
    }


    LaunchedEffect(Unit) {
        viewModel.loadFormState(transactionId, type)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "back"
                        )
                    }
                },
                title = stringResource(if (type) Screen.INCOME.titleRes else Screen.EXPENSES.titleRes)
            ) {
                IconButton(onClick = {
                    viewModel.editTransaction(transactionId=transactionId)

                }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "confirm"
                    )
                }
            }
        }
    ) { padding ->

        ResultStateHandler(
            state = state,
            onSuccess = { data ->
                TransactionForm(
                    transactionFormState = data,
                    currency = viewModel.currency(),
                    handleIntent = viewModel::handleIntent
                )
            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )


    }

}