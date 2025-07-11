package com.example.bankapp.features.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> LazyList(
    modifier: Modifier = Modifier,
    topItem: (@Composable () -> Unit)? = null,
    itemsList: List<T>,
    itemTemplate: @Composable (T) -> Unit,
    lastItemDivider: (@Composable () -> Unit) = {
        HorizontalDivider(
            Modifier
                .height(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
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


                if (index < itemsList.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                } else {
                    lastItemDivider()
                }
            }
        }
    }
}