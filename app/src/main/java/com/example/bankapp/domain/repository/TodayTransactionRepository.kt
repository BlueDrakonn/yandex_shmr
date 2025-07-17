package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Transaction

interface TodayTransactionRepository {

    suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>>


}