package com.example.bankapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountRequest(
    val name: String,
    val balance: String,
    val currency: String
)