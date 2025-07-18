package com.example.bankapp.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionRepository
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SyncDataWorker (
    context: Context,
    params: WorkerParameters,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {

        if (categoryRepository.loadCategories() !is ResultState.Success) return@coroutineScope Result.retry()

        if (accountRepository.loadAccounts() !is ResultState.Success) return@coroutineScope Result.retry()

        if (transactionRepository.loadHistoryTransaction(
                accountId = accountRepository.accountId,
                startDate = LocalDate.now().withDayOfYear(1).format(DateTimeFormatter.ISO_DATE),
                endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            ) !is ResultState.Success
        ) return@coroutineScope Result.retry()



        Result.success()
    }

}
