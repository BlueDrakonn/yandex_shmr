package com.example.bankapp.data.local.mappers

import com.example.bankapp.data.local.entity.AccountEntity
import com.example.bankapp.domain.model.Account

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        userId = this.id, //тут по идее id = userId тк счет один
        name = this.name,
        balance = this.balance,
        currency = this.currency
    )
}