package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.domain.model.TransactionEdit

interface TransactionActionRepository {
    suspend fun addTransaction(
        request: UpdateTransactionRequest
    ): ResultState<Unit>

    suspend fun editTransaction(
        transactionId: Int,
        request: UpdateTransactionRequest
    ): ResultState<Unit>

    suspend fun getTransactionById(
        transactionId : Int
    ): ResultState<TransactionEdit>

    suspend fun deleteTransactionById(
        transactionId : Int
    ): ResultState<Unit>
}