package com.example.bankapp.features.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> LazyList(
    modifier: Modifier = Modifier,
    topItem: (@Composable () -> Unit)? = null,
    itemsList: List<T>,
    onItemListIsEmpty: (@Composable () -> Unit)? = null,
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


        if (itemsList.isEmpty()) {
            onItemListIsEmpty?.let {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    it()
                }

            }
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