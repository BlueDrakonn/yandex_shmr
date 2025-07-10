//package com.example.bankapp.data.network.di
//
//import com.example.bankapp.data.repository.AccountRepositoryImpl
//import com.example.bankapp.data.repository.CategoryRepositoryImpl
//import com.example.bankapp.data.repository.HistoryTransactionRepositoryImpl
//import com.example.bankapp.data.repository.TodayTransactionRepositoryImpl
//import com.example.bankapp.data.repository.TransactionActionRepositoryImpl
//import com.example.bankapp.domain.repository.AccountRepository
//import com.example.bankapp.domain.repository.CategoryRepository
//import com.example.bankapp.domain.repository.HistoryTransactionRepository
//import com.example.bankapp.domain.repository.TodayTransactionRepository
//import com.example.bankapp.domain.repository.TransactionActionRepository
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.Binds
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//interface RepositoryModule {
//
//    @Binds
//    @Singleton
//    fun bindAccountRepository(
//        impl: AccountRepositoryImpl
//    ): AccountRepository
//
//    @Binds
//    fun bindCategoryRepository(
//        impl: CategoryRepositoryImpl
//    ): CategoryRepository
//
//    @Binds
//    fun bindHistoryTransactionRepository(
//        impl: HistoryTransactionRepositoryImpl
//    ): HistoryTransactionRepository
//
//    @Binds
//    fun bindTodayTransactionRepository(
//        impl: TodayTransactionRepositoryImpl
//    ): TodayTransactionRepository
//
//    @Binds
//    fun bindTransactionActionRepository(
//        impl: TransactionActionRepositoryImpl
//    ): TransactionActionRepository
//}