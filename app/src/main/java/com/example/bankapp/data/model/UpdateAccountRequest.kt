package com.example.bankapp.data.model

data class UpdateAccountRequest(
    val name: String,
    val balance: String,
    val currency: String
)