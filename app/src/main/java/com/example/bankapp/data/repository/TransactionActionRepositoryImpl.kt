package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateTransactionRequest
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.data.utils.safeApiCall
import com.example.bankapp.domain.model.TransactionEdit
import com.example.bankapp.domain.repository.TransactionActionRepository
import javax.inject.Inject

class TransactionActionRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    TransactionActionRepository {
    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<Unit> {
        return safeApiCall(
            mapper = {
            },
            block = {
                apiService.addTransactionById(
                    request = request
                )
            }
        )
    }

    override suspend fun editTransaction(transactionId: Int,request: UpdateTransactionRequest ): ResultState<Unit> {
        return safeApiCall(
            mapper = {
            },
            block = {
                apiService.updateTransactionById(
                    transactionId=transactionId,
                    request = request
                )
            }
        )
    }

    override suspend fun getTransactionById(transactionId: Int): ResultState<TransactionEdit> {
        return safeApiCall(
            mapper = {
                val transaction = it.toTransactionEdit()
                transaction
            },
            block = {
                apiService.getTransactionById(
                    transactionId=transactionId,
                )
            }
        )
    }

    override suspend fun deleteTransactionById(transactionId: Int): ResultState<Unit> {
        return safeApiCall(
            mapper = {
            },
            block = {
                apiService.deleteTransactionById(
                    transactionId=transactionId,
                )
            }
        )
    }
}