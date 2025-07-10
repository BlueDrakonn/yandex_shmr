package com.example.bankapp.features.transactionAction.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
fun formatTimePicker(timePickerState: TimePickerState): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
        set(Calendar.MINUTE, timePickerState.minute)
//        set(Calendar.SECOND, 0)
//        set(Calendar.MILLISECOND, 0)
    }

    val formatter = SimpleDateFormat("HH:mm", Locale.US).apply {
        //timeZone = TimeZone.getTimeZone("UTC")
    }

    return formatter.format(calendar.time)
}