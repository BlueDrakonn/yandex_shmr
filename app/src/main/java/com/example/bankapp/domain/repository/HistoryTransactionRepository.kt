package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.viewmodel.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface HistoryTransactionRepository {
    val startDate: StateFlow<String>
    val endDate: StateFlow<String>
    val transactionState: StateFlow<ResultState<List<TransactionDetailed>>>
    val totalIncomeState:  StateFlow<Double>
    val totalExpensesState:  StateFlow<Double>

    suspend fun loadHistoryTransaction(accountId: Int?)

    fun setStartDate(value: String)
    fun setEndDate(value: String)
}