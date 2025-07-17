package com.example.bankapp.di

import com.example.bankapp.data.repository.AccountRepositoryImpl
import com.example.bankapp.data.repository.CategoryRepositoryImpl
import com.example.bankapp.data.repository.TransactionActionRepositoryImpl
import com.example.bankapp.data.repository.TransactionRepositoryImpl
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.domain.repository.TransactionRepository
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
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindTransactionActionRepository(
        impl: TransactionActionRepositoryImpl
    ): TransactionActionRepository
}