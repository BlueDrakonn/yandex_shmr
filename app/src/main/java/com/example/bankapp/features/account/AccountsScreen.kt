package com.example.bankapp.features.account

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bankapp.R
import com.example.bankapp.features.common.ui.CurrencyBottomSheet
import com.example.bankapp.features.common.ui.LazyList
import com.example.bankapp.features.common.ui.LeadIcon
import com.example.bankapp.features.common.ui.PriceDisplay
import com.example.bankapp.features.common.ui.ResultStateHandler
import com.example.bankapp.features.common.ui.TrailingContent



@Composable
fun AccountsScreen(
    viewModel: AccountViewModel = hiltViewModel()
) {

    val state by viewModel.accountState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    ResultStateHandler(
        state = state,
        onSuccess = { data ->
            if (showBottomSheet) {
                CurrencyBottomSheet({showBottomSheet = false})
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