package com.example.bankapp.domain.mapper

import com.example.bankapp.data.local.models.TransactionWithCategory
import com.example.bankapp.data.remote.utils.parseIsoDateTime
import com.example.bankapp.domain.model.TransactionEdit
import java.time.format.DateTimeFormatter

fun TransactionWithCategory.toTransactionEdit(): TransactionEdit {
    val dateTime = parseIsoDateTime(transaction.transactionDate)

    return TransactionEdit(
        id = transaction.id,
        category = category.toDomain(),
        comment = transaction.subtitle,
        amount = transaction.amount,
        time = dateTime.second,
        date = dateTime.first // yyyy-MM-dd
    )
}