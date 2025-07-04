package com.example.bankapp.features.common.ui

import ListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bankapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyBottomSheet(
    onSelect: (String) -> Unit,
    onDismissRequest:() -> Unit ) {

    val list = listOf(
        Triple(R.drawable.rubble, R.string.bottom_sheet_rubble, "₽"),
        Triple(R.drawable.dollar, R.string.bottom_sheet_dollar, "$"),
        Triple(R.drawable.euro, R.string.bottom_sheet_euro, "€"),
        Triple(R.drawable.cancel, R.string.bottom_sheet_cancel,""),

    )

    ModalBottomSheet(onDismissRequest) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {

            list.forEachIndexed { index, (iconId,stringId, currency) ->

                val modifier = if (index == list.lastIndex) {
                    Modifier.background(MaterialTheme.colorScheme.error)
                } else {
                    Modifier
                }

                ListItem(
                    lead = {
                        Box(
                            Modifier.size(24.dp),
                            contentAlignment = Alignment.Center)
                        {
                        Icon(
                            painterResource(iconId),
                            contentDescription = null,
                            tint =
                                if(index == list.lastIndex) {
                                    MaterialTheme.colorScheme.background
                                } else MaterialTheme.colorScheme.onPrimary
                        )
                        }
                           },
                    content = {
                        Text(
                            text = stringResource(stringId),
                            color = if(index == list.lastIndex) {
                                MaterialTheme.colorScheme.onError
                            } else MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    modifier = modifier
                        .clickable {
                            onDismissRequest()
                            if (index < list.lastIndex) onSelect(currency)
                        }
                )

                if (index < list.lastIndex) {
                    HorizontalDivider()
                }

            }

        }
    }
}


