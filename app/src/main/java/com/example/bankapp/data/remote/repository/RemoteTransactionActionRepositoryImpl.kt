package com.example.bankapp.data.remote.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.api.ApiService
import com.example.bankapp.data.remote.model.TransactionDto
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.data.remote.utils.safeApiCall
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionEdit
import com.example.bankapp.domain.repository.TransactionActionRepository
import javax.inject.Inject

class RemoteTransactionActionRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    TransactionActionRepository {
    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<TransactionDto> {
        return safeApiCall(
            mapper = { it
            },
            block = {
                apiService.addTransactionById(
                    request = request
                )
            }
        )
    }

    override suspend fun editTransaction(transactionId: Int,request: UpdateTransactionRequest ): ResultState<Transaction> {
        return safeApiCall(
            mapper = { it.toTransaction()
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