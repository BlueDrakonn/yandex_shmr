package com.example.bankapp.features.income

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankapp.R
import com.example.bankapp.features.common.LazyList
import com.example.bankapp.features.common.PriceDisplay
import com.example.bankapp.features.common.ResultStateHandler
import com.example.bankapp.features.common.TrailingContent
@Composable
fun IncomeScreen(
    viewModel: IncomeViewModel = hiltViewModel()) {

    val mock by viewModel.transactionState.collectAsStateWithLifecycle()

    val totalAmount by viewModel.totalIncomeState.collectAsStateWithLifecycle()

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->

            LazyList(
                topItem = {
                    ListItem(
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
                        content = {
                            Text(
                                text = stringResource(R.string.totalAmount_subtitle),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        trailingContent = {
                            PriceDisplay(
                                amount = totalAmount.toString(),
                                currencySymbol = "₽" //пока мок версия
                            )
                        },
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    ListItem(
                        modifier = Modifier.height(68.dp).clickable {  },
                        content = {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        trailingContent = {
                            TrailingContent(
                                content = {
                                    PriceDisplay(
                                        amount = item.amount,
                                        currencySymbol = item.currency
                                    )
                                },
                                icon = {
                                    Icon(painter = painterResource(R.drawable.drillin),
                                        contentDescription = null,

                                        )
                                }
                            )

                        }
                    ) }
            )
        }
    )


}