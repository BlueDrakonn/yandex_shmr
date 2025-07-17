package com.example.bankapp.domain.model



data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val updatedAt: String? = null
)


