package com.example.bankapp.data.repository

import android.util.Log
import com.example.bankapp.core.ResultState
import com.example.bankapp.di.Local
import com.example.bankapp.di.NetworkChecker
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor( //тут по идее перед загрузкой сначала впихнуть все что у насв sync и после этого получить транзы и добавить тех что нет, а те что есть сравнить мб надо апгрейд (всегда надо тк только что из sync свои апргреды влили)
    @Local private val localTransactionRepositoryImpl: TransactionRepository,
    @Remote private val remoteTransactionRepositoryImpl: TransactionRepository,
    private val networkChecker: NetworkChecker
):  TransactionRepository {
    override suspend fun loadHistoryTransaction(
        accountId: Int?,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionDetailed>> {
        return if (networkChecker.isOnline()) {
            remoteTransactionRepositoryImpl.loadHistoryTransaction(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )
        } else {
            Log.d("ROME_ERROR222", "account local")
            localTransactionRepositoryImpl.loadHistoryTransaction(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    override suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>> {
        return if (networkChecker.isOnline()) {





            remoteTransactionRepositoryImpl.loadTodayTransaction(
                accountId = accountId,
            )
        } else {

            localTransactionRepositoryImpl.loadTodayTransaction(
                accountId = accountId,
            )
        }
    }

}