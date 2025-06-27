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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankapp.R
import com.example.bankapp.core.navigation.HistoryType
import com.example.bankapp.features.common.ui.DatePickerModal
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.PriceDisplay
import com.example.bankapp.features.common.ui.PriceWithDate
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.features.history.models.DateMode
import com.example.bankapp.features.history.models.DatePickerState


@Composable
fun HistoryScreen(
    type: HistoryType,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val transactions by viewModel.transactionState.collectAsStateWithLifecycle()
    val totalSum by  viewModel.totalAmountState.collectAsStateWithLifecycle()

    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(DatePickerState.CLOSED) }

    DisposableEffect(Unit) {

        viewModel.setHistoryType(type == HistoryType.INCOME)
        onDispose {
            viewModel.cancelGettingHistoryTransactions()
            viewModel.defaultDate()
        }
    }

    when(showDatePicker) {

        DatePickerState.CLOSED -> {}
        else -> DatePickerModal(
            onDateSelected = { date ->
                viewModel.updateDate(
                    if(showDatePicker == DatePickerState.OPEN_START) DateMode.START else DateMode.END,
                    date)
            },
            onDismiss = {
                showDatePicker = DatePickerState.CLOSED
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

                            showDatePicker = DatePickerState.OPEN_START

                                          },
                        endDataChange = {

                            showDatePicker = DatePickerState.OPEN_END

                                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                    )
                },
                itemsList = data,
                itemTemplate = { item->

                    ListItem(
                        modifier = Modifier
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