package com.example.bankapp.features.transactionAction.add.models

import com.example.bankapp.domain.model.Category

sealed class TransactionAddIntent {

    data class onAmountChanged(
        val amount: String
    ) : TransactionAddIntent()
    data class onTimeChanged(
        val time: String
    ) : TransactionAddIntent()
    data class onDateChanged(
        val date: String
    ) : TransactionAddIntent()
    data class onCommentChanged(
        val comment: String
    ) : TransactionAddIntent()
    data class onCategoryChanged(
        val category: Category
    ) : TransactionAddIntent()


}