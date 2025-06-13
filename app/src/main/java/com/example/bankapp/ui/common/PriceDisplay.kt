package com.example.bankapp.ui.common


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun PriceDisplay(
    amount: Double,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {

    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = ','
    }
    val formatter = DecimalFormat("#,##0", symbols)

    val formattedAmount = formatter.format(amount)

    Text(
        modifier = modifier,
        text = "$formattedAmount $currencySymbol",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimary
    )
}