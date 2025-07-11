package com.example.bankapp.features.account.account

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bankapp.R
import com.example.bankapp.core.navigation.Screen
import com.example.bankapp.di.LocalViewModelFactory
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.PriceDisplay
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent
import com.example.bankapp.navigation.TopAppBar


@Composable
fun AccountsScreen(

    navController: NavHostController
) {
    val viewModel: AccountViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.accountState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Screen.ACCOUNTS.titleRes)
            ) {
                IconButton(onClick = {
                    navController.navigate(Screen.ACCOUNTS_EDIT.route)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.edit), contentDescription = "edit"
                    )
                }
            }
        }) { padding ->
        ResultStateHandler(
            state = state, onSuccess = { data ->

                LazyList(itemsList = data, lastItemDivider = {}, itemTemplate = { item ->
                    ListItem(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable { }, lead = {
                            LeadIcon(
                                backGroundColor = MaterialTheme.colorScheme.background,
                                label = "💰"
                            )
                        }, content = {
                            Text(item.name)
                        }, trailingContent = {
                            TrailingContent(content = {
                                PriceDisplay(
                                    amount = item.balance,
                                    currencySymbol = item.currency,
                                )
                            }, icon = {
                                Icon(
                                    painter = painterResource(R.drawable.drillin),
                                    contentDescription = null,
                                )
                            })

                        }

                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ListItem(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable {}, content = {
                            Text(
                                text = stringResource(R.string.currency),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }, trailingContent = {
                            TrailingContent(
                                content = {
                                    Text(
                                        text = item.currency,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.drillin),
                                        contentDescription = null,
                                    )
                                },
                            )
                        })
                })
            }, modifier = Modifier.padding(top = padding.calculateTopPadding())
        )
    }


}