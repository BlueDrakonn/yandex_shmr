package com.example.bankapp.domain.model

data class TransactionEdit(
    val id: Int,
    val category: Category,
    val comment: String? = null,
    val amount: String,
    val time: String,
    val date: String,
)