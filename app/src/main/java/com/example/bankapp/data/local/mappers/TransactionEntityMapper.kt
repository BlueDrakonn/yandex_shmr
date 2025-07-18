package com.example.bankapp.data.local.mappers

import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.remote.model.TransactionDto

fun TransactionDto.toEntity(isIncome: Boolean): TransactionEntity {
    return TransactionEntity(
        id = id,
        categoryId = categoryId,
        subtitle = comment,
        amount = amount,
        transactionDate = transactionDate,
        isIncome = isIncome
    )
}