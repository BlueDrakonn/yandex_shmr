package com.example.bankapp.domain.model

data class Transaction (
    val id: Int,
    val category: Category,
    val subtitle: String? = null,
    val amount: String,
    val transactionDate: String,
    override val isIncome: Boolean

): IncomeInfo

