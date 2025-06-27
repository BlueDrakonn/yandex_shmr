package com.example.bankapp.data.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.TodayTransactionRepository
import com.example.bankapp.utils.safeApiCall
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TodayTransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): TodayTransactionRepository {

    override suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>> {

        return  safeApiCall(
            mapper = {
                val transaction = it.toTransaction()

                transaction
            },
            block = {
                apiService.getTransactions(
                    accountId = accountId!!,
                    startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                    endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                )
            }
        )
    }
}