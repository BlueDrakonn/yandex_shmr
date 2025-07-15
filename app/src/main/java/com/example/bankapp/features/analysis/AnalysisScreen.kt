package com.example.bankapp.features.analysis

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.core.navigation.TransactionType
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.analysis.models.AnalysisIntent
import com.example.bankapp.features.common.ui.DatePickerModal
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.PriceDisplay
import com.example.bankapp.features.common.ui.PriceWithDate
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.features.common.utlis.formatDate
import com.example.bankapp.features.history.HistoryTopBlock
import com.example.bankapp.features.history.HistoryViewModel
import com.example.bankapp.features.history.models.DateMode
import com.example.bankapp.features.history.models.DatePickerState
import com.example.bankapp.navigation.TopAppBar


@Composable
fun AnalysisScreen(
    navController: NavHostController,
    isIncomeTransactions: Boolean){

    val viewModel: AnalysisViewModel = viewModel(factory = LocalViewModelFactory.current)

    var showDatePicker by remember { mutableStateOf(DatePickerState.CLOSED) }

    val formState by viewModel.analysisFormState.collectAsState()
    

    DisposableEffect(Unit) {

        viewModel.analysisIntentHandler(analysisIntent =  AnalysisIntent.setAnalysisType(analysisType = isIncomeTransactions))
        onDispose {
            viewModel.analysisIntentHandler(analysisIntent = AnalysisIntent.onDismiss)
        }
    }

    when (showDatePicker) {

        DatePickerState.CLOSED -> {}
        else -> DatePickerModal(
            onDateSelected = { date ->
                viewModel.analysisIntentHandler(analysisIntent = AnalysisIntent.onUpdateDate(
                    mode = if (showDatePicker == DatePickerState.OPEN_START) DateMode.START else DateMode.END,
                    date = date
                ))
                showDatePicker = DatePickerState.CLOSED

            },
            onDismiss = {
                showDatePicker = DatePickerState.CLOSED
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate to previous screen"
                        )
                    }
                },
                title = stringResource(Screen.HISTORY_INCOME.titleRes),
                action = {
                    //логика с графиком
                }
            )
        }
    ) { padding ->
        ResultStateHandler(
            state = formState.transactionRequestState,
            onSuccess = { data ->
                LazyList(
                    topItem = {
                        AnalysisTopBlock(
                            startData = formState.startDate,
                            endData = formState.endDate,
                            totalSum = formState.totalAmount,
                            currency = data.firstOrNull()?.transactionDetailed?.currency ?: "",
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
                    itemTemplate = { item ->
                        val transaction = item.transactionDetailed
                        val percent = item.percent

                        ListItem(
                            modifier = Modifier
                                .clickable {  },
                            lead = { transaction.icon?.let { LeadIcon(label = it) } },
                            content = {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = transaction.category.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    if (transaction.subtitle != null) {
                                        Text(
                                            text = transaction.subtitle,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                            },
                            trailingContent = {
                                TrailingContent(
                                    content = {
                                        Column(horizontalAlignment = Alignment.End) {

                                            Text("$percent %")
                                            PriceWithDate(
                                                date = formatDate(transaction.transactionDate),
                                                price = {
                                                    PriceDisplay(
                                                        amount = transaction.amount,
                                                        currencySymbol = transaction.currency
                                                    )
                                                }
                                            )
                                        }


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
            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )
    }

}


@Composable
fun AnalysisTopBlock(
    startData: String,
    endData: String,
    totalSum: Double,
    currency: String,
    startDataChange: () -> Unit,
    endDataChange: () -> Unit,
    modifier: Modifier = Modifier
) {
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