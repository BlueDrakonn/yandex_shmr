package com.example.bankapp.domain.mapper

import com.example.bankapp.data.local.models.TransactionWithCategory
import com.example.bankapp.domain.model.Transaction

fun TransactionWithCategory.toTransaction(): Transaction {
    return Transaction(
        id = transaction.id,
        category = category.toDomain(),
        subtitle = transaction.subtitle,
        amount = transaction.amount,
        isIncome = transaction.isIncome,
        transactionDate = transaction.transactionDate
    )
}