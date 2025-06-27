package com.example.bankapp.data.repository


import android.util.Log
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import com.example.bankapp.utils.safeApiCall
import javax.inject.Inject

class HistoryTransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): HistoryTransactionRepository {

    override suspend fun loadHistoryTransaction(
        accountId: Int,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionDetailed>> {

        return safeApiCall(
            mapper = {
               it.toTransactionDetailed()
            },
            block = {apiService.getTransactions(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )
            }
        )
    }

}


