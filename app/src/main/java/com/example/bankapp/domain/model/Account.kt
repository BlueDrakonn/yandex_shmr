package com.example.bankapp.domain.model

import java.time.LocalDateTime


data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
)


