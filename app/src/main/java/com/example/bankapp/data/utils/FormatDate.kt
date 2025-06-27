package com.example.bankapp.data.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(input: String): String {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("d MMMM  HH:mm", Locale("ru"))

    val zonedDateTime = ZonedDateTime.parse(input, inputFormatter)
    return outputFormatter.format(zonedDateTime)
}