package com.example.bankapp.features.transactionAction.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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


enum class ACTION {
    DELETE,
    EDIT,
    NOTHING
}

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

    val action = remember { mutableStateOf(ACTION.NOTHING) }
    when (requestState) {
        is RequestState.Error -> {

            val errorMessage = (requestState as RequestState.Error).message

            ErrorDialog(
                message = errorMessage,
                onRetry = {
                    when (action.value) {
                        ACTION.EDIT -> {
                            viewModel.editTransaction(transactionId = transactionId)
                        }

                        ACTION.DELETE -> {
                            viewModel.deleteTransaction(transactionId = transactionId)
                        }

                        ACTION.NOTHING -> {}
                    }
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
            when (action.value) {
                ACTION.EDIT -> {
                    showToast(context, context.getString(R.string.transaction_edited_successful))
                }

                ACTION.DELETE -> {
                    showToast(context, context.getString(R.string.transaction_deleted_successful))
                }

                ACTION.NOTHING -> {}
            }

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
                    action.value = ACTION.EDIT
                    viewModel.editTransaction(transactionId = transactionId)

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TransactionForm(
                        transactionFormState = data,
                        currency = viewModel.currency(),
                        handleIntent = viewModel::handleIntent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            action.value = ACTION.DELETE
                            viewModel.deleteTransaction(transactionId = transactionId)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                        ) {
                        Text(
                            text = stringResource(R.string.transaction_delete),
                        )
                    }

                }

            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )


    }

}