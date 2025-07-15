package com.example.bankapp.data.model

import com.example.bankapp.data.utils.getCurrencySymbol
import com.example.bankapp.data.utils.parseIsoDateTime
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.model.TransactionEdit

data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
)

data class TransactionResponseDto(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toTransaction(): Transaction {
        return Transaction(
            id = id,
            category = category,
            subtitle = comment,
            icon = category.emoji,
            amount = amount.toString(),
            currency = getCurrencySymbol(account.currency),
            isIncome = category.isIncome
        )
    }
    fun toTransactionDetailed(): TransactionDetailed {
        return TransactionDetailed(
            id = id,
            category = category,
            subtitle = comment,
            icon = category.emoji,
            amount = amount.toString(),
            currency = account.currency,
            createdAt = createdAt,
            updatedAt = updatedAt,
            transactionDate= transactionDate,
            isIncome = category.isIncome
        )
    }
    fun toTransactionEdit(): TransactionEdit {

        val result = parseIsoDateTime(transactionDate)

        return TransactionEdit(
            id = id,
            category = category,
            comment = comment,
            amount = amount.toString(),
            time = result.second,
            date = result.first
        )
    }



}



