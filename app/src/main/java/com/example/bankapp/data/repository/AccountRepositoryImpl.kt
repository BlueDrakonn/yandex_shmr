package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.repository.LocalTransactionRepositoryImpl
import com.example.bankapp.data.remote.model.UpdateAccountRequest
import com.example.bankapp.di.Local
import com.example.bankapp.di.NetworkChecker
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.SyncOperationRepository
import com.example.bankapp.domain.repository.TransactionRepository
import com.example.bankapp.domain.repository.WriteRepository
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    @Local private val localAccountRepositoryImpl: AccountRepository,
    @Remote private val remoteAccountRepositoryImpl: AccountRepository,
    private val writeAccountRepositoryImpl: WriteRepository<Account>,
    private val syncOperationRepositoryImpl: SyncOperationRepository,
    private val networkChecker: NetworkChecker
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

        if (networkChecker.isOnline()) {
            var remoteResult = remoteAccountRepositoryImpl.loadAccounts()

            when (remoteResult) {
                is ResultState.Success -> {
                    val localResult = syncOperationRepositoryImpl.getPendingOperationsByType(
                        OperationType.UPDATE_ACCOUNT,
                        targetId = remoteResult.data.firstOrNull()?.id ?: 28
                    )

                    if (localResult != null) {

                        if (localResult.createdAt > remoteResult.data.firstOrNull()!!.updatedAt!!) {
                            val request: UpdateAccountRequest =
                                Json.decodeFromString(localResult.payload)
                            remoteAccountRepositoryImpl.updateAccount(request = request)
                            syncOperationRepositoryImpl.removeOperation(
                                listOf(OperationType.UPDATE_ACCOUNT),
                                targetId = remoteResult.data.firstOrNull()?.id ?: 28
                            )
                            remoteResult = remoteAccountRepositoryImpl.loadAccounts()



                        } else {
                            syncOperationRepositoryImpl.removeOperation(
                                listOf(OperationType.UPDATE_ACCOUNT),
                                targetId = remoteResult.data.firstOrNull()?.id ?: 28
                            )
                            writeAccountRepositoryImpl.addDb(remoteResult.data.firstOrNull()!!)
                        }


                    } else {
                        writeAccountRepositoryImpl.addDb(remoteResult.data.firstOrNull()!!)
                    }

                }

                else -> {}
            }


            accountCurrency = remoteAccountRepositoryImpl.accountCurrency
            accountId = remoteAccountRepositoryImpl.accountId
            accountError = remoteAccountRepositoryImpl.accountError
            return remoteResult
        } else {

            val result = localAccountRepositoryImpl.loadAccounts()
            accountCurrency = localAccountRepositoryImpl.accountCurrency
            accountId = localAccountRepositoryImpl.accountId
            accountError = localAccountRepositoryImpl.accountError
            return result
        }

    }

    override suspend fun updateAccount(request: UpdateAccountRequest): ResultState<Account> {

        return if (networkChecker.isOnline()) {

            remoteAccountRepositoryImpl.updateAccount(request=request)


        } else {
            localAccountRepositoryImpl.updateAccount(request = request)
        }

    }

    override suspend fun loadTransactionsForChart(): ResultState<List<TransactionDetailed>> {
        return if (networkChecker.isOnline()) {

            remoteAccountRepositoryImpl.loadTransactionsForChart()


        } else {
            localAccountRepositoryImpl.loadTransactionsForChart()
        }
    }


}