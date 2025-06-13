package com.example.bankapp.ui.screen

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bankapp.R
import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.ui.common.LazyList
import com.example.bankapp.ui.common.LeadIcon
import com.example.bankapp.ui.common.PriceDisplay
import com.example.bankapp.ui.common.ResultStateHandler
import com.example.bankapp.ui.common.TrailingContent

@Composable
fun ExpensesScreen(viewModel: MainViewModel) {

    val mock by viewModel.expenseTransactions.collectAsState()

    val totalAmount by viewModel.totalExpense.collectAsState()

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
                                amount = totalAmount,
                                currencySymbol = "₽" //пока мок версия тк не очень понятно какой значок отображать ессли список пустой, если расходы привязаны к счету, то можно валюту ссчета, но пока ноль инфы про это
                            )
                        },
                    )

                },
                itemsList = data,
                itemTemplate = { item ->

                    ListItem(
                        modifier = Modifier.height(68.dp).clickable {  },
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
                                content = {PriceDisplay(
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

                        },

                    )
                }
            )
        }
    )
}

