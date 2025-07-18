package com.example.bankapp.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.dao.TransactionDao
import com.example.bankapp.domain.mapper.toTransaction
import com.example.bankapp.domain.mapper.toTransactionDetailed
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.TransactionRepository
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

class LocalTransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {


    override suspend fun loadHistoryTransaction(
        accountId: Int?,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionDetailed>> {

        try {
            val result = transactionDao.getTransactionsBetweenDates(
                startDate = startDate,
                endDate = endDate
            )

            return ResultState.Success(data = result.map { it.toTransactionDetailed() })

        } catch (e: SQLiteConstraintException) {

            return ResultState.Error(message = e.message)
        }


    }

    override suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>> {

        try {
            val result = transactionDao.getTransactionsBetweenDates(
                startDate = LocalDate.now(ZoneOffset.UTC)
                    .atStartOfDay()
                    .atOffset(ZoneOffset.UTC)
                    .toInstant()
                    .toString(),
                endDate = LocalDate.now(ZoneOffset.UTC).atTime(23, 59, 59, 999_000_000)
                    .atOffset(ZoneOffset.UTC)
                    .toInstant()
                    .toString()
            )

            return ResultState.Success(data = result.map { it.toTransaction() })

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }

    }


}