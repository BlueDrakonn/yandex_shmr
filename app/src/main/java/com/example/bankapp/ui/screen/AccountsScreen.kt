package com.example.bankapp.ui.screen

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.bankapp.R
import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.ui.common.LazyList
import com.example.bankapp.ui.common.LeadIcon
import com.example.bankapp.ui.common.PriceDisplay
import com.example.bankapp.ui.common.ResultStateHandler
import com.example.bankapp.ui.common.TrailingContent


@Composable
fun AccountsScreen(viewModel: MainViewModel) {

    val mock by viewModel.accounts.collectAsState()

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->
            LazyList(
                itemsList = data,
                lastItemDivider = {},
                itemTemplate = { item ->
                    ListItem(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable {  },
                        lead = {
                            LeadIcon(
                                backGroundColor = MaterialTheme.colorScheme.background,
                                label = "💰"
                            )
                        },
                        content = {
                            Text(stringResource(R.string.balance))
                        },
                        trailingContent = {
                            TrailingContent(
                                content =  {
                                    PriceDisplay(
                                        amount = item.balance,
                                        currencySymbol = item.currency,
                                    )
                                },
                                icon = {
                                    Icon(painter = painterResource(R.drawable.drillin),
                                        contentDescription = null,
                                    )
                                }
                            )

                        }

                    )
                    HorizontalDivider( color = MaterialTheme.colorScheme.outlineVariant)

                    ListItem(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable {  },
                        content = {
                            Text(
                                text = stringResource(R.string.currency),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        trailingContent = {

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


                        }

                    )

                }
            )
        }

    )


}