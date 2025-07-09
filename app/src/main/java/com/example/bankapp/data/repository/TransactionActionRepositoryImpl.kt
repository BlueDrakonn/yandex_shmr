package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateTransactionRequest
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.data.utils.safeApiCall
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.TransactionActionRepository
import javax.inject.Inject

class TransactionActionRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    TransactionActionRepository {
    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<Transaction> {
        return safeApiCall(
            mapper = {
                val transaction = it.toTransaction()
                transaction
            },
            block = {
                apiService.addTransaction(
                    request = request
                )
            }
        )
    }

    override suspend fun editTransaction(transactionId: Int,request: UpdateTransactionRequest ): ResultState<Transaction> {
        return safeApiCall(
            mapper = {
                val transaction = it.toTransaction()
                transaction
            },
            block = {
                apiService.updateTransaction(
                    transactionId=transactionId,
                    request = request
                )
            }
        )
    }
}