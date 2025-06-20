package com.example.bankapp.ui.screen

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankapp.R

import com.example.bankapp.domain.viewmodel.MainViewModel
import com.example.bankapp.ui.common.CurrencyChangeBottomSheet
import com.example.bankapp.ui.common.LazyList
import com.example.bankapp.ui.common.LeadIcon
import com.example.bankapp.ui.common.PriceDisplay
import com.example.bankapp.ui.common.ResultStateHandler
import com.example.bankapp.ui.common.TrailingContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(viewModel: MainViewModel) {


    val mock by viewModel.observeAccounts().collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->


            if (showBottomSheet) {

                CurrencyChangeBottomSheet({showBottomSheet = false})
            }

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
                            .clickable { showBottomSheet = true  },
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