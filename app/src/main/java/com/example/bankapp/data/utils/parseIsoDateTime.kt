package com.example.bankapp.data.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun parseIsoDateTime(isoString: String): Pair<String, String> {
    val instant = Instant.parse(isoString)
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())

    val date = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val time = zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    return date to time
}