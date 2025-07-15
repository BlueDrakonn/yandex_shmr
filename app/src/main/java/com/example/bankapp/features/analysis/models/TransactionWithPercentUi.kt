package com.example.bankapp.features.analysis.models

import com.example.bankapp.domain.model.TransactionDetailed

data class TransactionWithPercentUi(
    val transactionDetailed: TransactionDetailed,
    val percent: Float
)