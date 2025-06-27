package com.example.bankapp.domain.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.TransactionDetailed


interface HistoryTransactionRepository {

    suspend fun loadHistoryTransaction(
        accountId: Int,
        startDate: String,
        endDate: String): ResultState<List<TransactionDetailed>>

}