package com.example.bankapp.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> LazyList(
    modifier: Modifier = Modifier,
    topItem: (@Composable () -> Unit)? = null,
    itemsList: List<T>,
    itemTemplate: @Composable (T) -> Unit,
    lastItemDivider: (@Composable () -> Unit) = {HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)}
) {
    Column(
        modifier = modifier
    ) {
        topItem?.let {
            it()
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }

        LazyColumn {
            itemsIndexed(itemsList) { index, item ->
                itemTemplate(item)

                // проверка ласт элема
                if (index < itemsList.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                } else {
                    lastItemDivider()
                }
            }
        }
    }

}