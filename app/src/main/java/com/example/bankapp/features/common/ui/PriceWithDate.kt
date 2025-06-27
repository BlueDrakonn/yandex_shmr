package com.example.bankapp.features.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun PriceWithDate(
    date: String,
    price: @Composable () -> Unit
){
    Column(horizontalAlignment = Alignment.End) {

        price()

        Text(text = date,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge)


    }

}