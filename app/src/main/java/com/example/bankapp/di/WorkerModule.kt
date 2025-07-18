package com.example.bankapp.di

import androidx.work.WorkerFactory
import com.example.bankapp.background.AppWorkerFactory
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides

@Module
class WorkerModule {
    @Provides
    fun provideWorkerFactory(
        categoryRepository: CategoryRepository,
        accountRepository: AccountRepository,
        transactionRepository: TransactionRepository
    ): WorkerFactory {
        return AppWorkerFactory(
            categoryRepository,
            accountRepository,
            transactionRepository
        )
    }
}
