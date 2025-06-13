package com.example.bankapp.data.model

import com.example.bankapp.domain.model.Category
import java.time.LocalDateTime

data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
)

data class TransactionResponse(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
