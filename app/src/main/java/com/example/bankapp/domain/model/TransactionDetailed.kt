package com.example.bankapp.domain.model

data class TransactionDetailed(
    val id: Int,
    val title: String,
    val subtitle: String? = null,
    val icon: String? = null,
    val amount: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String,
    val transactionDate: String,
    override val isIncome: Boolean
): IncomeInfo