package com.example.bankapp.features.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun PriceDisplay(
    amount: String,
    modifier: Modifier = Modifier,
    currencySymbol: String? = "",

) {

    val currency = when (currencySymbol) {
        "RUB" -> "\u20BD" // ₽
        "USD" -> "\u0024" // $
        "EUR" -> "\u20AC" // €
        else -> currencySymbol
    }

    Text(
        modifier = modifier,
        text = "$amount $currency",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimary
    )
}