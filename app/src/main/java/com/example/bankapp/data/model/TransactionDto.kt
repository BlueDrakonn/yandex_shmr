package com.example.bankapp.data.model

import com.example.bankapp.domain.model.Transaction

data class TransactionDto(
    val id : Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toTransaction(): Transaction {
        return Transaction(
            id = id,
            title = "",
            subtitle = "",
            icon = "",
            amount = amount.toString(),
            currency = "",
            isIncome = false
        )
    }
}