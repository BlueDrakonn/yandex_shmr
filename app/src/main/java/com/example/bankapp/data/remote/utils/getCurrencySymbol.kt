package com.example.bankapp.data.remote.utils

fun getCurrencySymbol(currencyCode: String): String {
    return when (currencyCode) {
        "RUB" -> "\u20BD" // ₽
        "USD" -> "\u0024" // $
        "EUR" -> "\u20AC" // €
        else -> currencyCode
    }
}