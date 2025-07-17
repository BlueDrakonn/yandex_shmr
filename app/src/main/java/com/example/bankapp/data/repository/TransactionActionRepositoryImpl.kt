package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.di.Local
import com.example.bankapp.di.NetworkChecker
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.model.TransactionEdit
import com.example.bankapp.domain.repository.TransactionActionRepository
import javax.inject.Inject

class TransactionActionRepositoryImpl @Inject constructor(
    @Remote private val remoteTransactionActionRepositoryImpl: TransactionActionRepository,
    @Local private val localTransactionActionRepositoryImpl: TransactionActionRepository,
    private val networkChecker: NetworkChecker
) : TransactionActionRepository {

    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<Unit> {
        return if (networkChecker.isOnline()) {
            remoteTransactionActionRepositoryImpl.addTransaction(request)
        } else {
            localTransactionActionRepositoryImpl.addTransaction(request)
        }
    }

    override suspend fun editTransaction(
        transactionId: Int,
        request: UpdateTransactionRequest
    ): ResultState<Unit> {
        return if (networkChecker.isOnline()) {
            remoteTransactionActionRepositoryImpl.editTransaction(
                transactionId = transactionId,
                request = request
            )
        } else {
            localTransactionActionRepositoryImpl.editTransaction(
                transactionId = transactionId,
                request = request
            )
        }
    }

    override suspend fun getTransactionById(transactionId: Int): ResultState<TransactionEdit> {
        return  if(networkChecker.isOnline()) {
            remoteTransactionActionRepositoryImpl.getTransactionById(transactionId=transactionId)
        } else {
            localTransactionActionRepositoryImpl.getTransactionById(transactionId=transactionId)
        }
    }

    override suspend fun deleteTransactionById(transactionId: Int): ResultState<Unit> {
        if (networkChecker.isOnline()) {
            val result = remoteTransactionActionRepositoryImpl.deleteTransactionById(transactionId=transactionId)
            return result
        } else {
            return localTransactionActionRepositoryImpl.deleteTransactionById(transactionId=transactionId)
        }
    }
}