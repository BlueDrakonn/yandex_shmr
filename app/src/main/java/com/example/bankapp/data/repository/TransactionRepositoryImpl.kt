package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.local.mappers.toEntity
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.di.Local
import com.example.bankapp.di.NetworkChecker
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.SyncOperationRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.domain.repository.TransactionRepository
import com.example.bankapp.domain.repository.WriteRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor( //тут по идее перед загрузкой сначала впихнуть все что у насв sync и после этого получить транзы и добавить тех что нет, а те что есть сравнить мб надо апгрейд (всегда надо тк только что из sync свои апргреды влили)
    @Local private val localTransactionRepositoryImpl: TransactionRepository,
    @Local private val localTransactionActionRepositoryImpl: TransactionActionRepository,
    @Remote private val remoteTransactionRepositoryImpl: TransactionRepository,
    @Remote private val remoteTransactionActionRepositoryImpl: TransactionActionRepository,
    private val writeRepository: WriteRepository<TransactionEntity>,
    private val syncOperationRepositoryImpl: SyncOperationRepository,
    private val networkChecker: NetworkChecker
) : TransactionRepository {
    override suspend fun loadHistoryTransaction(
        accountId: Int?,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionDetailed>> {
        return if (networkChecker.isOnline()) {

            syncOperationRepositoryImpl.getPendingOperations().forEach { operation ->

                when (operation.type) {
                    OperationType.ADD_TRANSACTION -> {
                        val result = remoteTransactionActionRepositoryImpl.addTransaction(
                            request = Json.decodeFromString<UpdateTransactionRequest>(operation.payload)
                        )
                        if (result is ResultState.Success) {
                            localTransactionActionRepositoryImpl.editTransaction(
                                transactionId = result.data!!.id,
                                request = Json.decodeFromString<UpdateTransactionRequest>(operation.payload)
                            )
                            syncOperationRepositoryImpl.removeOperation(
                                listOf(operation.type),
                                targetId = operation.targetId!!
                            )
                        }
                    }

                    OperationType.UPDATE_TRANSACTION -> {

                        //здесь добавить проверку на какие изменения позже были
                        remoteTransactionActionRepositoryImpl.editTransaction(
                            transactionId = operation.targetId!!,
                            request = Json.decodeFromString<UpdateTransactionRequest>(operation.payload)
                        )
                    }

                    OperationType.DELETE_TRANSACTION -> {
                        remoteTransactionActionRepositoryImpl.deleteTransactionById(operation.targetId!!)

                        syncOperationRepositoryImpl.removeOperation(
                            listOf(operation.type),
                            targetId = operation.targetId
                        )
                    }

                    OperationType.UPDATE_ACCOUNT -> {}
                }

            }


            val result = remoteTransactionRepositoryImpl.loadHistoryTransaction(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )
            when (result) {
                is ResultState.Success -> {
                    result.data.forEach { transaction ->
                        writeRepository.addDb(
                            entity = TransactionEntity(
                                id = transaction.id,
                                categoryId = transaction.category.id,
                                subtitle = transaction.subtitle,
                                amount = transaction.amount,
                                transactionDate = transaction.transactionDate,
                                isIncome = transaction.isIncome
                            )
                        )
                    }
                }

                else -> {}
            }
            result


        } else {

            localTransactionRepositoryImpl.loadHistoryTransaction(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    override suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>> {
        return if (networkChecker.isOnline()) {

            syncOperationRepositoryImpl.getPendingOperations().forEach { operation ->

                when (operation.type) {
                    OperationType.ADD_TRANSACTION -> {
                        val result = remoteTransactionActionRepositoryImpl.addTransaction(
                            request = Json.decodeFromString<UpdateTransactionRequest>(operation.payload)
                        )
                        if (result is ResultState.Success) {
                            localTransactionActionRepositoryImpl.editTransaction(
                                transactionId = result.data!!.id,
                                request = Json.decodeFromString<UpdateTransactionRequest>(operation.payload)
                            )
                            syncOperationRepositoryImpl.removeOperation(
                                listOf(operation.type),
                                targetId = operation.targetId!!
                            )
                        }
                    }

                    OperationType.UPDATE_TRANSACTION -> {
                        val remoteResult =
                            remoteTransactionActionRepositoryImpl.getTransactionById(transactionId = operation.targetId!!)

                        if (remoteResult is ResultState.Success) {

                            if (operation.createdAt > remoteResult.data.updatedAt) {
                                remoteTransactionActionRepositoryImpl.editTransaction(
                                    transactionId = operation.targetId!!,
                                    request = Json.decodeFromString<UpdateTransactionRequest>(
                                        operation.payload
                                    )
                                )
                            } else {
                                writeRepository.addDb(remoteResult.data.toEntity())
                            }
                        }

                        syncOperationRepositoryImpl.removeOperation(
                            listOf(operation.type),
                            targetId = operation.targetId
                        )
                    }

                    OperationType.DELETE_TRANSACTION -> {
                        remoteTransactionActionRepositoryImpl.deleteTransactionById(operation.targetId!!)

                        syncOperationRepositoryImpl.removeOperation(
                            listOf(operation.type),
                            targetId = operation.targetId
                        )
                    }

                    OperationType.UPDATE_ACCOUNT -> {}
                }

            }


            val result = remoteTransactionRepositoryImpl.loadTodayTransaction(
                accountId = accountId,
            )
            when (result) {
                is ResultState.Success -> {
                    result.data.forEach { transaction ->
                        writeRepository.addDb(
                            entity = TransactionEntity(
                                id = transaction.id,
                                categoryId = transaction.category.id,
                                subtitle = transaction.subtitle,
                                amount = transaction.amount,
                                transactionDate = transaction.transactionDate,
                                isIncome = transaction.isIncome
                            )
                        )
                    }
                }

                else -> {}
            }

            result
        } else {

            localTransactionRepositoryImpl.loadTodayTransaction(
                accountId = accountId,
            )
        }
    }

}