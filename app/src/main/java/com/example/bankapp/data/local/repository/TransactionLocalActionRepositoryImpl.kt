package com.example.bankapp.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.dao.AccountDao
import com.example.bankapp.data.local.dao.CategoryDao
import com.example.bankapp.data.local.dao.SyncOperationDao
import com.example.bankapp.data.local.dao.TransactionDao
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.SyncOperationEntity
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.data.remote.utils.safeApiCall
import com.example.bankapp.domain.mapper.toTransactionDetailed
import com.example.bankapp.domain.mapper.toTransactionEdit
import com.example.bankapp.domain.mapper.toTransactionEntity
import com.example.bankapp.domain.model.TransactionEdit
import com.example.bankapp.domain.repository.TransactionActionRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import javax.inject.Inject

class TransactionLocalActionRepositoryImpl @Inject constructor(
    private val syncOperationDao: SyncOperationDao,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    ) :
    TransactionActionRepository {
    override suspend fun addTransaction(request: UpdateTransactionRequest): ResultState<Unit> {

        try {
            val isIncome = categoryDao.getCategoryById(
                id = request.categoryId
            ).isIncome

            val currency = accountDao.getAllAccounts().first().currency

            val transactionLocalId = (transactionDao.getMinTransactionId()   ?: 0 ) -1



            val transactionEntity = request.toTransactionEntity(
                id = transactionLocalId,
                currency = currency,
                isIncome = isIncome
            )
            transactionDao.insertTransaction(transaction = transactionEntity)

            syncOperationDao.insertOperation(
                operation = SyncOperationEntity(
                    type = OperationType.ADD_TRANSACTION,
                    payload = Json.encodeToString(request),
                    createdAt = Instant.now().toString(),
                    targetId = transactionLocalId
                )
            )

            return ResultState.Success(Unit)

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }

    override suspend fun editTransaction(transactionId: Int,request: UpdateTransactionRequest): ResultState<Unit> {
        try {


            val isIncome = categoryDao.getCategoryById(
                id = request.categoryId
            ).isIncome

            val currency = accountDao.getAllAccounts().first().currency


            val transactionEntity = request.toTransactionEntity(
                id = transactionId,
                currency = currency,
                isIncome = isIncome
            )
            transactionDao.updateTransaction(transaction = transactionEntity)


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

            return ResultState.Success(Unit)

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
}