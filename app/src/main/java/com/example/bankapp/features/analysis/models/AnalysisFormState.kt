package com.example.bankapp.features.analysis.models

import com.example.bankapp.core.ResultState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AnalysisFormState(
    val startDate: String = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE),
    val endDate: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
    val totalAmount: Double = 0.0,
    val transactionRequestState: ResultState<List<AnalysisCategoryUi>> = ResultState.Loading
)



