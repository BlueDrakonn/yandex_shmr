package com.example.bankapp.domain.mapper

import com.example.bankapp.data.local.entity.AccountEntity
import com.example.bankapp.domain.model.Account

fun AccountEntity.toDomain(): Account {
    return Account(
        userId = this.userId,
        name = this.name,
        balance = this.balance,
        currency = this.currency,
        id = 28
    )
}