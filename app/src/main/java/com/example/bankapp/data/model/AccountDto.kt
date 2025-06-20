package com.example.bankapp.data.model

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
            currency = currency
        )
    }
}

//fun AccountDto.toAccount(): Account {
//    return Account(
//        id = id,
//        userId = userId,
//        name = name,
//        balance = balance,
//        currency = currency
//    )
//}

