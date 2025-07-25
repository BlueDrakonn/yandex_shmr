package com.example.bankapp.features.analysis.models

import com.example.bankapp.domain.model.Category

data class AnalysisCategoryUi(
    val category: Category,
    val amount: Double,
    val percent: Float
)