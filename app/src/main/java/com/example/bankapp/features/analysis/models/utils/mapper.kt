package com.example.bankapp.features.analysis.models.utils

import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.features.analysis.models.TransactionWithPercentUi

fun List<TransactionDetailed>.toUiModel(): List<TransactionWithPercentUi> {
    val total = this.sumOf { it.amount.toDouble()}


    return this.map { txn ->
        val amount = txn.amount.toFloatOrNull() ?: 0f
        val percent = if (total == 0.0) 0f else (amount / total.toFloat()) * 100f
        TransactionWithPercentUi(transactionDetailed = txn, percent = percent)
    }
}
