package com.example.bankapp.features.analysis.models

import com.example.bankapp.core.ResultState
import com.google.common.base.Objects
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AnalysisFormState(
    val startDate: String = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE),
    val endDate: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
    val totalAmount : Double = 0.0,
    val transactionRequestState: ResultState<List<TransactionWithPercentUi>> = ResultState.Loading
)

//sealed class AnalysisFormState<out T> {
//    data class Success(
//        val data: List<TransactionWithPercentUi>,
//        var startDate: String = LocalDate.now().withDayOfMonth(1)
//            .format(DateTimeFormatter.ISO_DATE),
//        var endDate: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
//        var totalAmount: String = "",
//    ) : AnalysisFormState<TransactionWithPercentUi>()
//
//    data class Error(
//        val message: String?,
//        val code: Int? = null,
//        val throwable: Throwable? = null
//    ) : AnalysisFormState<Nothing>()
//
//    object Loading : AnalysisFormState<Nothing>()
//
//}

