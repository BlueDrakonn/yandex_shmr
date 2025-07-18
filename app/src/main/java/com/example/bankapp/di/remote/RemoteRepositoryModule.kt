package com.example.bankapp.di.remote

import com.example.bankapp.data.remote.repository.RemoteAccountRepositoryImpl
import com.example.bankapp.data.remote.repository.RemoteCategoryRepositoryImpl
import com.example.bankapp.data.remote.repository.RemoteTransactionActionRepositoryImpl
import com.example.bankapp.data.remote.repository.RemoteTransactionRepositoryImpl
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RemoteRepositoryModule {

    @Binds
    @Singleton
    @Remote
    abstract fun bindRemoteAccountRepository(
        impl: RemoteAccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    @Remote
    abstract fun bindRemoteCategoryRepository(
        impl: RemoteCategoryRepositoryImpl
    ): CategoryRepository


    @Binds
    @Singleton
    @Remote
    abstract fun bindRemoteTransactionRepository(
        impl: RemoteTransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    @Remote
    abstract fun bindRemoteTransactionActionRepository(
        impl: RemoteTransactionActionRepositoryImpl
    ): TransactionActionRepository


}