package com.example.bankapp.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.dao.AccountDao
import com.example.bankapp.data.local.dao.SyncOperationDao
import com.example.bankapp.data.local.entity.AccountEntity
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.SyncOperationEntity
import com.example.bankapp.data.remote.model.UpdateAccountRequest
import com.example.bankapp.domain.mapper.toDomain
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import javax.inject.Inject

class AccountLocalRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val syncOperationDao: SyncOperationDao,
) : AccountRepository {


    /**
     * ID текущего  аккаунта.
     */
    override var accountId: Int? = null

    /**
     * Currency текущего  аккаунта.
     */
    override var accountCurrency: String? = null

    /**
     * Error возникший при получение аккаунта, прокидывается на другие скрины.
     */
    override var accountError: String? = null

    override suspend fun loadAccounts(): ResultState<List<Account>> {


        try {
            val result = accountDao.getAllAccounts().map {it.toDomain()  }
            accountId = result.firstOrNull()?.userId
            accountCurrency = result.firstOrNull()?.currency
            return ResultState.Success(data = result)
        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }

    }

    override suspend fun updateAccount(
        request: UpdateAccountRequest
    ): ResultState<Account> {



        try {
            accountDao.updateAccount(
                account = AccountEntity(
                    userId = accountId ?: 28,
                    name = request.name,
                    balance = request.balance,
                    currency = request.currency
                )
            )

            syncOperationDao.deleteTransactionOperations( //удаляем прошлые ее обновления
                transactionId = accountId ?: 28,
                types = listOf(
                    OperationType.UPDATE_ACCOUNT
                )
            )

            syncOperationDao.insertOperation(
                operation = SyncOperationEntity(
                    type = OperationType.UPDATE_ACCOUNT,
                    payload = Json.encodeToString(request),
                    createdAt = Instant.now().toString(),
                    targetId = accountId ?: 28
                )
            )
            return ResultState.Success(
                Account(
                    id = accountId ?: 28,
                    userId = accountId ?: 28,
                    name = request.name,
                    balance = request.balance,
                    currency = request.currency
                )
            )
        } catch (e: SQLiteConstraintException) {

            return ResultState.Error(message = e.message)
        }



    }

}