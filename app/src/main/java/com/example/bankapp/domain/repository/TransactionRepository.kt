package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed

interface TransactionRepository {

    suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>>

    suspend fun loadHistoryTransaction(
        accountId: Int?,
        startDate: String,
        endDate: String): ResultState<List<TransactionDetailed>>


}