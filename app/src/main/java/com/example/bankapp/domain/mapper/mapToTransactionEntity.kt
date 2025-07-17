package com.example.bankapp.domain.mapper

import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.remote.model.UpdateTransactionRequest

fun UpdateTransactionRequest.toTransactionEntity(
    id: Int,
    currency: String,
    isIncome: Boolean
): TransactionEntity {

    return TransactionEntity(
        id = id,
        categoryId = categoryId,
        subtitle = comment,
        amount = amount,
        currency = currency,
        transactionDate = transactionDate,
        isIncome = isIncome
    )
}

