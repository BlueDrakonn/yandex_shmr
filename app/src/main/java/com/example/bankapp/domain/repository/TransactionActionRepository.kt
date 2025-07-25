package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.model.TransactionDto
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed

interface TransactionActionRepository {
    suspend fun addTransaction(
        request: UpdateTransactionRequest
    ): ResultState<TransactionDto?>

    suspend fun editTransaction(
        transactionId: Int,
        request: UpdateTransactionRequest
    ): ResultState<Transaction?>

    suspend fun getTransactionById(
        transactionId : Int
    ): ResultState<TransactionDetailed>

    suspend fun deleteTransactionById(
        transactionId : Int
    ): ResultState<Unit>
}