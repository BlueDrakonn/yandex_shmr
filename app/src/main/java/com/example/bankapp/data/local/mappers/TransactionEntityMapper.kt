package com.example.bankapp.data.local.mappers

import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.remote.model.TransactionDto
import com.example.bankapp.domain.model.TransactionDetailed

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
fun TransactionDetailed.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        categoryId = category.id,
        subtitle = subtitle,
        amount = amount,
        transactionDate = transactionDate,
        isIncome = isIncome
    )
}