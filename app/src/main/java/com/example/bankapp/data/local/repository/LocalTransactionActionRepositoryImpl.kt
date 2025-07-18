package com.example.bankapp.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.dao.CategoryDao
import com.example.bankapp.data.local.dao.SyncOperationDao
import com.example.bankapp.data.local.dao.TransactionDao
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.SyncOperationEntity
import com.example.bankapp.data.local.mappers.toEntity
import com.example.bankapp.data.remote.model.TransactionDto
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.di.DefaultNetworkChecker
import com.example.bankapp.domain.mapper.toTransactionEdit
import com.example.bankapp.domain.mapper.toTransactionEntity
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionEdit
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.domain.repository.WriteRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import javax.inject.Inject

class LocalTransactionActionRepositoryImpl @Inject constructor(
    private val syncOperationDao: SyncOperationDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val networkChecker: DefaultNetworkChecker
) :
    TransactionActionRepository, WriteRepository<TransactionDto> {
    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<TransactionDto?> {

        try {
            val isIncome = categoryDao.getCategoryById(
                id = request.categoryId
            ).isIncome


            val transactionLocalId = (transactionDao.getMinTransactionId() ?: 0) - 1


            val transactionEntity = request.toTransactionEntity(
                id = transactionLocalId,
                isIncome = isIncome
            )
            transactionDao.insertTransaction(transaction = transactionEntity)

            if (!networkChecker.isOnline()) {
                syncOperationDao.insertOperation(
                    operation = SyncOperationEntity(
                        type = OperationType.ADD_TRANSACTION,
                        payload = Json.encodeToString(request),
                        createdAt = Instant.now().toString(),
                        targetId = transactionLocalId
                    )
                )
            }

            return ResultState.Success(null)

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }

    override suspend fun editTransaction(
        transactionId: Int,
        request: UpdateTransactionRequest
    ): ResultState<Transaction?> {
        try {


            val isIncome = categoryDao.getCategoryById(
                id = request.categoryId
            ).isIncome


            val transactionEntity = request.toTransactionEntity(
                id = transactionId,

                isIncome = isIncome
            )
            transactionDao.updateTransaction(transaction = transactionEntity)


            if (!networkChecker.isOnline()) {
                syncOperationDao.deleteTransactionOperations( //удаляем прошлые ее обновления
                    transactionId = transactionId,
                    types = listOf(
                        OperationType.UPDATE_TRANSACTION
                    )
                )

                syncOperationDao.insertOperation(
                    operation = SyncOperationEntity(
                        type = OperationType.UPDATE_TRANSACTION,
                        payload = Json.encodeToString(request),
                        createdAt = Instant.now().toString(),
                        targetId = transactionId
                    )
                )
            }

            return ResultState.Success(null)

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }

    override suspend fun getTransactionById(transactionId: Int): ResultState<TransactionEdit> {
        try {

            val result = transactionDao.getTransactionById(id = transactionId)

            if (result != null) {
                return ResultState.Success(result.toTransactionEdit())
            } else {
                return ResultState.Error(message = "не найдена транзакция")
            }

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }

    override suspend fun deleteTransactionById(transactionId: Int): ResultState<Unit> {
        try {
            transactionDao.deleteTransactionById(id = transactionId)


            syncOperationDao.deleteTransactionOperations( //удаляем все операции связанные с транзой все равно ее удалим
                transactionId = transactionId,
                types = listOf(
                    OperationType.ADD_TRANSACTION,
                    OperationType.UPDATE_TRANSACTION
                )
            )

            if (transactionId >= 0) { //если локальную транзу удаляем то с сервера ее удалять не надо

                syncOperationDao.insertOperation(
                    operation = SyncOperationEntity(
                        type = OperationType.DELETE_TRANSACTION,
                        payload = transactionId.toString(),
                        createdAt = Instant.now().toString(),
                    )
                )
            }
            return ResultState.Success(Unit)

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }

    override suspend fun addDb(entity: TransactionDto) {

        val isIncome = categoryDao.getCategoryById(
            id = entity.categoryId
        ).isIncome

        val transactionEntity = entity.toEntity(isIncome = isIncome)

        transactionDao.insertTransaction(transaction = transactionEntity)
    }
}