package com.example.bankapp.features.history

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankapp.R
import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.features.common.DatePickerModal
import com.example.bankapp.features.common.LazyList
import com.example.bankapp.features.common.LeadIcon
import com.example.bankapp.features.common.PriceDisplay
import com.example.bankapp.features.common.PriceWithDate
import com.example.bankapp.features.common.ResultStateHandler
import com.example.bankapp.features.common.TrailingContent


enum class TransactionType {
    INCOME, EXPENSE
}

@Composable
fun HistoryScreen(type: TransactionType, viewModel: MainViewModel) {

    val transactions by when (type) {
        TransactionType.INCOME -> viewModel.observeHistoryIncome().collectAsStateWithLifecycle()
        TransactionType.EXPENSE -> viewModel.observeHistoryExpenses().collectAsStateWithLifecycle()
    }

    val totalSum by when (type) {
        TransactionType.INCOME -> viewModel.observeHistoryTotalIncome().collectAsStateWithLifecycle()
        TransactionType.EXPENSE -> viewModel.observeHistoryTotalExpenses().collectAsStateWithLifecycle()
    }

    val endDate by viewModel.observeEndDate().collectAsStateWithLifecycle()
    val startDate by viewModel.observeStartDate().collectAsStateWithLifecycle()


    var showDatePicker by remember { mutableStateOf(false) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {

        viewModel.startGettingHistoryTransactions()
        onDispose {
            viewModel.cancelGettingHistoryTransactions()
            viewModel.defaultDate()
        }
    }



    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                if (showStartPicker) {
                    viewModel.changeStartDate(date)
                    viewModel.startGettingHistoryTransactions()
                } else {
                    viewModel.changeEndDate(date)
                    viewModel.startGettingHistoryTransactions()
                }
            },
            onDismiss = {
                showDatePicker = false
                showEndPicker = false
                showStartPicker = false
            }
        )
    }

    ResultStateHandler(
        state = transactions,
        onSuccess = { data ->
            LazyList(
                topItem = {
                    HistoryTopBlock(
                        startData = startDate,
                        endData = endDate,
                        totalSum = totalSum,
                        currency = data.firstOrNull()?.currency ?: "",
                        startDataChange = {
                            showStartPicker = true
                            showDatePicker = true

                                          },
                        endDataChange = {
                            showEndPicker = true
                            showDatePicker = true

                                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                    )
                },
                itemsList = data,
                itemTemplate = { item->

                    ListItem(
                        modifier = Modifier
                            //.height(70.dp)
                            .clickable { },
                        lead = { item.icon?.let { LeadIcon(label = it) } },
                        content = {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                if (item.subtitle != null) {
                                    Text(
                                        text = item.subtitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        },
                        trailingContent = {
                            TrailingContent(
                                content = {

                                    PriceWithDate(
                                        date = item.transactionDate,
                                        price = { PriceDisplay(
                                            amount = item.amount,
                                            currencySymbol = item.currency
                                        ) }
                                    )

                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.drillin),
                                        contentDescription = null,

                                        )
                                }
                            )
                        }

                    )
                },

            )
        }
    )
}

@Composable
fun HistoryTopBlock(
    startData: String,
    endData: String,
    totalSum: Double,
    currency: String,
    startDataChange: () -> Unit,
    endDataChange: () -> Unit,
    modifier: Modifier = Modifier
){
    Column {

        ListItem(
            content = {
                Text(
                    text = stringResource(R.string.history_start),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                Text(
                    text = startData,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = modifier.clickable { startDataChange() }
        )
        HorizontalDivider()
        ListItem(
            content = {
                Text(
                    text = stringResource(R.string.history_end),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                Text(
                    text = endData,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = modifier.clickable { endDataChange() }
        )
        HorizontalDivider()
        ListItem(
            content = {
                Text(
                    text = stringResource(R.string.history_sum),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                PriceDisplay(
                    amount = totalSum.toString(),
                    currencySymbol = currency,
                )
            },
            modifier = modifier
        )
    }

}