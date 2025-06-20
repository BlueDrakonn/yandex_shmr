package com.example.bankapp.data.model

import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
            title = category.name,
            subtitle = comment,
            icon = category.emoji,
            amount = amount.toString(),
            currency = account.currency,
            isIncome = category.isIncome
        )
    }
    fun toTransactionDetailed(): TransactionDetailed {
        return TransactionDetailed(
            id = id,
            title = category.name,
            subtitle = comment,
            icon = category.emoji,
            amount = amount.toString(),
            currency = account.currency,
            createdAt = formatDate(createdAt),
            updatedAt = formatDate(updatedAt),
            transactionDate= formatDate(transactionDate),
            isIncome = category.isIncome
        )
    }


}


fun formatDate(input: String): String {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("d MMMM  HH:mm", Locale("ru"))

    val zonedDateTime = ZonedDateTime.parse(input, inputFormatter)
    return outputFormatter.format(zonedDateTime)
}