package com.example.bankapp.features.transactionAction.add

import ListItem
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.account.accountEdit.showToast
import com.example.bankapp.features.common.ui.DatePickerModal
import com.example.bankapp.features.common.ui.DialUseStateExample
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.features.transactionAction.add.models.TransactionAddIntent
import com.example.bankapp.features.transactionAction.add.models.TransactionFormState
import com.example.bankapp.features.transactionAction.models.RequestState
import com.example.bankapp.features.transactionAction.ui.ErrorDialog
import com.example.bankapp.features.transactionAction.utils.formatTimePicker
import com.example.bankapp.navigation.TopAppBar
import kotlinx.coroutines.launch


@Composable
fun TransactionAddScreen(

    navController: NavHostController,
    type: Boolean,
) {
    val viewModel: TransactionAddViewModel = viewModel(factory = LocalViewModelFactory.current)
    val context = LocalContext.current
    val state by viewModel.categoryState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()


    when (requestState) {
        is RequestState.Error -> {

            val errorMessage = (requestState as RequestState.Error).message
            Log.d("ERROR_TRANS",errorMessage)
            ErrorDialog(
                message = errorMessage,
                onRetry = {
                    coroutineScope.launch {
                        val result = viewModel.addTransaction()
                    }
                },
                onCancel = {
                    viewModel.requestDialogDismiss()
                },
            )
        }
        RequestState.Idle -> {}
        RequestState.Loading -> CircularProgressIndicator()
        RequestState.Success -> {showToast(context, context.getString(R.string.transaction_added_successful))
            navController.popBackStack()}
    }

    LaunchedEffect(Unit) {
        viewModel.loadCategories(type)
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
                    coroutineScope.launch {
                        val result = viewModel.addTransaction()

                    }

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
                TransactionAddForm(
                    transactionFormState = data,
                    viewModel = viewModel,
                )
            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAddForm(
    transactionFormState: TransactionFormState,
    viewModel: TransactionAddViewModel,
) {

    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }



    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->

                viewModel.handleIntent(TransactionAddIntent.onDateChanged(date))
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            })
    }

    if (showTimePicker) {

        DialUseStateExample(
            onConfirm = { time ->

                viewModel.handleIntent(TransactionAddIntent.onTimeChanged(formatTimePicker(time)))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }



    Column(
    ) {
        ListItem(
            modifier = Modifier.height(70.dp),
            lead = { Text(text = stringResource(R.string.transaction_action_category)) },
            content = { Box(Modifier.fillMaxWidth()) },
            trailingContent = {


                TrailingContent(
                    content = {
                        Box {

                            Text(text = transactionFormState.selectedCategory.name)
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.heightIn(max = 200.dp)
                            ) {
                                transactionFormState.categoryList.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            viewModel.handleIntent(
                                                TransactionAddIntent.onCategoryChanged(
                                                    it
                                                )
                                            )
                                            expanded = false
                                        },
                                        text = { Text(it.name) })
                                }
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.drillin),
                            contentDescription = null,

                            )
                    },
                    modifier = Modifier
                        .clickable { expanded = true }

                )

            }

        )
        HorizontalDivider()
        ListItem(
            modifier = Modifier.height(70.dp),
            lead = {
                Text(text = stringResource(R.string.transaction_action_amount))
            },
            trailingContent = {
                TrailingContent(
                    content = {
                        BasicTextField(
                            value = transactionFormState.amount,
                            onValueChange = {
                                viewModel.handleIntent(
                                    TransactionAddIntent.onAmountChanged(
                                        it
                                    )
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.End
                            ),
                            singleLine = true

                        )
                    },
                    icon = { Text(text = viewModel.currency()) }

                )

            }

        )
        HorizontalDivider()
        ListItem(
            modifier = Modifier
                .height(70.dp)
                .clickable { showDatePicker = true },
            lead = {
                Text(text = stringResource(R.string.transaction_action_date))
            },
            trailingContent = {
                Text(text = transactionFormState.date)
            }

        )
        HorizontalDivider()
        ListItem(
            modifier = Modifier
                .height(70.dp)
                .clickable { showTimePicker = true },
            lead = {
                Text(text = stringResource(R.string.transaction_action_time))
            },
            trailingContent = {
                Text(text = transactionFormState.time)
            }

        )
        HorizontalDivider()
        ListItem(
            modifier = Modifier
                .height(70.dp),
            lead = {
                Text(text = stringResource(R.string.transaction_action_comment))
            },
            content = {
                BasicTextField(
                    value = transactionFormState.comment,
                    onValueChange = {
                        viewModel.handleIntent(
                            TransactionAddIntent.onCommentChanged(
                                it
                            )
                        )
                    },

                    )
            }

        )
        HorizontalDivider()
    }
}



