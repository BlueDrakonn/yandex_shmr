package com.example.bankapp.domain.model

data class Transaction (
    val id: Int,
    val title: String,
    val subtitle: String? = null,
    val icon: String? = null,
    val amount: String,
    val currency: String,
    override val isIncome: Boolean

): IncomeInfo