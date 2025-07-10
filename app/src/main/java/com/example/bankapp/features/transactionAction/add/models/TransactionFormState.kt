package com.example.bankapp.features.transactionAction.add.models

import com.example.bankapp.domain.model.Category

data class TransactionFormState(
    var categoryList: List<Category> = listOf(),
    var selectedCategory: Category,
    var date: String = "",
    var time: String = "",
    var comment: String = "",
    var amount: String = ""
)