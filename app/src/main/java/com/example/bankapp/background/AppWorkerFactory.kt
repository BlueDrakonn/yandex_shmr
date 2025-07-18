package com.example.bankapp.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionRepository
import javax.inject.Inject

class AppWorkerFactory @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParams: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncDataWorker::class.java.name -> {
                SyncDataWorker(
                    appContext,
                    workerParams,
                    categoryRepository,
                    accountRepository,
                    transactionRepository
                )
            }
            else -> null
        }
    }
}

