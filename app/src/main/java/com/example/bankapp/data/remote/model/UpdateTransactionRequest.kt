package com.example.bankapp.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class UpdateTransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)