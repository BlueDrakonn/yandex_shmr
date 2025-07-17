package com.example.bankapp.data.remote.model

import com.example.bankapp.data.remote.utils.getCurrencySymbol
import com.example.bankapp.domain.model.Account

data class AccountDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toAccount(): Account {
        return Account(
            id = id,
            userId = userId,
            name = name,
            balance = balance,
            currency = getCurrencySymbol(currency),
            updatedAt = updatedAt
        )
    }
}


