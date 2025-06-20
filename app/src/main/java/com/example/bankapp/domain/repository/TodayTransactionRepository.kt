package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.viewmodel.ResultState
import kotlinx.coroutines.flow.StateFlow

interface TodayTransactionRepository {

    val totalIncomeState:  StateFlow<Double>
    val totalExpensesState:  StateFlow<Double>

    val transactionState: StateFlow<ResultState<List<Transaction>>>
    suspend fun loadTodayTransaction(accountId: Int?)
}