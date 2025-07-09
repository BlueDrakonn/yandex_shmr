package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateTransactionRequest
import com.example.bankapp.domain.model.Transaction

interface TransactionActionRepository {
    suspend fun addTransaction(
        request: UpdateTransactionRequest
    ): ResultState<Transaction>

    suspend fun editTransaction(
        transactionId: Int,
        request: UpdateTransactionRequest
    ): ResultState<Transaction>
}