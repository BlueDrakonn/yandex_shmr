package com.example.bankapp.data.model

data class UpdateTransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)