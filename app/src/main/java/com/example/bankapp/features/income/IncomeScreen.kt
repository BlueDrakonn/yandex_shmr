package com.example.bankapp.features.income

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.common.ui.AddButton
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.PriceDisplay
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.navigation.TopAppBar

@Composable
fun IncomeScreen(

    navController: NavHostController
) {
    val viewModel: IncomeViewModel = viewModel(factory = LocalViewModelFactory.current)

    val resultState by viewModel.transactionState.collectAsStateWithLifecycle()

    val totalAmount by viewModel.totalIncomeState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Screen.INCOME.titleRes),
                action = {
                    IconButton(
                        onClick = { navController.navigate(Screen.HISTORY_INCOME.route) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.history),
                            contentDescription = "history"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AddButton(onClick = {
                navController.navigate("${Screen.TRANSACTION_ADD.route}?type=${true}")
            })
        }
    ) { padding ->
        ResultStateHandler(
            state = resultState,
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
                                    currencySymbol = viewModel.currentCurrency()
                                )
                            },
                        )
                    },
                    itemsList = data,
                    itemTemplate = { item ->
                        ListItem(
                            modifier = Modifier
                                .height(68.dp)
                                .clickable { navController.navigate("${Screen.TRANSACTION_EDIT.route}?type=${true}?transactionId=${item.id}")},
                            content = {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = item.category.name,
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
                                        Icon(
                                            painter = painterResource(R.drawable.drillin),
                                            contentDescription = null,

                                            )
                                    }
                                )

                            }
                        )
                    }
                )
            },
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        )
    }


}