package com.example.bankapp.data.remote.model

data class TransactionDto(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val createdAt: String,
    val updatedAt: String
)