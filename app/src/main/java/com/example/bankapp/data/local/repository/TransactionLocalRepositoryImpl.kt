package com.example.bankapp.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.dao.TransactionDao
import com.example.bankapp.domain.mapper.toTransaction
import com.example.bankapp.domain.mapper.toTransactionDetailed
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import com.example.bankapp.domain.repository.TodayTransactionRepository
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionLocalRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : HistoryTransactionRepository, TodayTransactionRepository {


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
                startDate = Instant.now()
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                endDate = Instant.now()
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )

            return ResultState.Success(data = result.map { it.toTransaction() })

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }

    }
}