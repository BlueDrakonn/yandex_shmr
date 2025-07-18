package com.example.bankapp.features.analysis

import ListItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.analysis.models.AnalysisIntent
import com.example.bankapp.features.analysis.models.utils.formatToFullDate
import com.example.bankapp.features.common.ui.DatePickerModal
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.PriceDisplay
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.features.history.models.DateMode
import com.example.bankapp.features.history.models.DatePickerState
import com.example.bankapp.navigation.TopAppBar


@Composable
fun AnalysisScreen(
    navController: NavHostController,
    isIncomeTransactions: Boolean
) {

    val viewModel: AnalysisViewModel = viewModel(factory = LocalViewModelFactory.current)

    var showDatePicker by remember { mutableStateOf(DatePickerState.CLOSED) }

    val formState by viewModel.analysisFormState.collectAsState()


    DisposableEffect(Unit) {

        viewModel.analysisIntentHandler(analysisIntent = AnalysisIntent.setAnalysisType(analysisType = isIncomeTransactions))
        onDispose {
            viewModel.analysisIntentHandler(analysisIntent = AnalysisIntent.onDismiss)
        }
    }

    when (showDatePicker) {

        DatePickerState.CLOSED -> {}
        else -> DatePickerModal(
            onDateSelected = { date ->
                viewModel.analysisIntentHandler(
                    analysisIntent = AnalysisIntent.onUpdateDate(
                        mode = if (showDatePicker == DatePickerState.OPEN_START) DateMode.START else DateMode.END,
                        date = date
                    )
                )
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
                title = stringResource(Screen.ANALYSIS.titleRes),
                action = {

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
                            currency = viewModel.getCurrency(),
                            startDataChange = {

                                showDatePicker = DatePickerState.OPEN_START

                            },
                            endDataChange = {

                                showDatePicker = DatePickerState.OPEN_END

                            }
                        )
                    },
                    itemsList = data,
                    onItemListIsEmpty = {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.analysis_empty_transaction_list))
                        }

                    },
                    itemTemplate = { item ->
                        val category = item.category
                        val amount = item.amount
                        val percent = item.percent

                        ListItem(
                            modifier = Modifier
                                .clickable { },
                            lead = { LeadIcon(label = category.emoji) },
                            content = {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                }
                            },
                            trailingContent = {
                                TrailingContent(
                                    content = {
                                        Column(horizontalAlignment = Alignment.End) {

                                            Text("$percent %")
                                            PriceDisplay(
                                                amount = amount.toString(),
                                                currencySymbol = viewModel.getCurrency()
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
) {
    Column {

        ListItem(
            content = {
                Text(
                    text = stringResource(R.string.analysis_start),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                DateChip(
                    date = startData.formatToFullDate(),
                    onClick = startDataChange
                )
            },
            modifier = Modifier.height(57.dp)
        )
        HorizontalDivider()
        ListItem(
            content = {
                Text(
                    text = stringResource(R.string.analysis_end),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                DateChip(
                    date = endData.formatToFullDate(),
                    onClick = endDataChange
                )
            },

            modifier = Modifier.height(57.dp)
        )
        HorizontalDivider()
        ListItem(
            content = {
                Text(
                    text = stringResource(R.string.analysis_sum),
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
            modifier = Modifier.height(57.dp)
        )
    }

}

@Composable
fun DateChip(date: String, onClick: () -> Unit) {
    AssistChip(
        onClick = { onClick() },
        label = {
            Text(
                text = date,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(16.dp)
    )
}