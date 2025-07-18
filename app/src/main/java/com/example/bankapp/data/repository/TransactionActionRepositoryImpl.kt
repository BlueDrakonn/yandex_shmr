package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.remote.model.TransactionDto
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.di.Local
import com.example.bankapp.di.NetworkChecker
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionEdit
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.domain.repository.WriteRepository
import javax.inject.Inject

class TransactionActionRepositoryImpl @Inject constructor(
    @Remote private val remoteTransactionActionRepositoryImpl: TransactionActionRepository,
    @Local private val localTransactionActionRepositoryImpl: TransactionActionRepository,
    private val writeCategoryRepositoryImpl: WriteRepository<TransactionEntity>,
    private val networkChecker: NetworkChecker
) : TransactionActionRepository {

    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<TransactionDto?> {
        return if (networkChecker.isOnline()) {

            val result = remoteTransactionActionRepositoryImpl.addTransaction(request)
            if (result is ResultState.Success) {
                val transactionDto =result.data!!
                writeCategoryRepositoryImpl.addDb(entity = TransactionEntity(
                    id = transactionDto.id,
                    categoryId = transactionDto.categoryId,
                    subtitle = transactionDto.comment,
                    amount = transactionDto.amount,
                    transactionDate = transactionDto.transactionDate,
                    isIncome = true //костыль внутри addDb isIncome сам выставляется
                ))
            }
            ResultState.Success(null)
        } else {
            localTransactionActionRepositoryImpl.addTransaction(request)
        }
    }

    override suspend fun editTransaction(
        transactionId: Int,
        request: UpdateTransactionRequest
    ): ResultState<Transaction?> {
        if (networkChecker.isOnline()) {
            val result = remoteTransactionActionRepositoryImpl.editTransaction(
                transactionId = transactionId,
                request = request
            )
            if (result is ResultState.Success) {
                localTransactionActionRepositoryImpl.editTransaction( //тут по идее можем редактировать транзу которой нет в бд или нет?
                    transactionId = transactionId,
                    request = request
                )

            }
            return ResultState.Success(null)

        } else {
            return localTransactionActionRepositoryImpl.editTransaction(
                transactionId = transactionId,
                request = request
            )
        }
    }

    override suspend fun getTransactionById(transactionId: Int): ResultState<TransactionEdit> {
        return if (networkChecker.isOnline()) {
            remoteTransactionActionRepositoryImpl.getTransactionById(transactionId = transactionId)
        } else {
            localTransactionActionRepositoryImpl.getTransactionById(transactionId = transactionId)
        }
    }

    override suspend fun deleteTransactionById(transactionId: Int): ResultState<Unit> {
        if (networkChecker.isOnline()) {
            val result =
                remoteTransactionActionRepositoryImpl.deleteTransactionById(transactionId = transactionId)

            if (result is ResultState.Success) {
                localTransactionActionRepositoryImpl.deleteTransactionById(transactionId = transactionId)
            }
            return result
        } else {
            return localTransactionActionRepositoryImpl.deleteTransactionById(transactionId = transactionId)
        }
    }
}