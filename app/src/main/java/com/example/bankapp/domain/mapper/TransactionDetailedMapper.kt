package com.example.bankapp.domain.mapper

import com.example.bankapp.data.local.models.TransactionWithCategory
import com.example.bankapp.domain.model.TransactionDetailed

fun TransactionWithCategory.toTransactionDetailed(): TransactionDetailed {
    return TransactionDetailed(
        id = transaction.id,
        category = category.toDomain(),
        subtitle = transaction.subtitle,
        icon = category.emoji,
        amount = transaction.amount,
        currency = transaction.currency,
        createdAt = transaction.transactionDate,
        updatedAt = transaction.transactionDate,
        transactionDate = transaction.transactionDate,
        isIncome = transaction.isIncome
    )
}