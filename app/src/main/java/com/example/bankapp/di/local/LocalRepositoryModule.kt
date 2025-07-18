package com.example.bankapp.di.local

import com.example.bankapp.data.local.repository.LocalAccountRepositoryImpl
import com.example.bankapp.data.local.repository.LocalCategoryRepositoryImpl
import com.example.bankapp.data.local.repository.LocalTransactionActionRepositoryImpl
import com.example.bankapp.data.local.repository.LocalTransactionRepositoryImpl
import com.example.bankapp.data.local.repository.SyncOperationRepositoryImpl
import com.example.bankapp.data.remote.model.TransactionDto
import com.example.bankapp.di.Local
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.SyncOperationRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.domain.repository.TransactionRepository
import com.example.bankapp.domain.repository.WriteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class LocalRepositoryModule {
    @Binds
    @Singleton
    @Local
    abstract fun bindLocalAccountRepository(
        impl: LocalAccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    @Local
    abstract fun bindLocalCategoryRepository(
        impl: LocalCategoryRepositoryImpl
    ): CategoryRepository



    @Binds
    @Singleton
    @Local
    abstract fun bindLocalTransactionRepository(
        impl: LocalTransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    @Local
    abstract fun bindLocalTransactionActionRepository(
        impl: LocalTransactionActionRepositoryImpl
    ): TransactionActionRepository

    @Binds
    @Singleton
    abstract fun bindSyncOperationRepository(
        impl: SyncOperationRepositoryImpl
    ): SyncOperationRepository



    ////исправить
    @Binds
    @Singleton
    abstract fun bindAccountWriteRepository(
        impl: LocalAccountRepositoryImpl
    ): WriteRepository<Account>

    ////исправить
    @Binds
    @Singleton
    abstract fun bindCategoryWriteRepository(
        impl: LocalCategoryRepositoryImpl
    ): WriteRepository<Category>

    @Binds
    @Singleton
    abstract fun bindTransactionActionWriteRepository(
        impl: LocalTransactionActionRepositoryImpl
    ): WriteRepository<TransactionDto>
}