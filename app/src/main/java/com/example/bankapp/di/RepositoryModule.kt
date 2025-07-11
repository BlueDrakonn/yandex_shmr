package com.example.bankapp.di

import com.example.bankapp.data.repository.AccountRepositoryImpl
import com.example.bankapp.data.repository.CategoryRepositoryImpl
import com.example.bankapp.data.repository.HistoryTransactionRepositoryImpl
import com.example.bankapp.data.repository.TodayTransactionRepositoryImpl
import com.example.bankapp.data.repository.TransactionActionRepositoryImpl
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import com.example.bankapp.domain.repository.TodayTransactionRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindHistoryTransactionRepository(
        impl: HistoryTransactionRepositoryImpl
    ): HistoryTransactionRepository

    @Binds
    @Singleton
    abstract fun bindTodayTransactionRepository(
        impl: TodayTransactionRepositoryImpl
    ): TodayTransactionRepository

    @Binds
    @Singleton
    abstract fun bindTransactionActionRepository(
        impl: TransactionActionRepositoryImpl
    ): TransactionActionRepository


}