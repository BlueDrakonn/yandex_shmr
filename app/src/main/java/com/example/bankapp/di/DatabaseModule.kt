package com.example.bankapp.di

import android.content.Context
import androidx.room.Room
import com.example.bankapp.data.local.AppDatabase
import com.example.bankapp.data.local.dao.AccountDao
import com.example.bankapp.data.local.dao.CategoryDao
import com.example.bankapp.data.local.dao.SyncOperationDao
import com.example.bankapp.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "local_database.db"
        ).build()
    }

    @Provides
    fun provideAccountDao(database: AppDatabase): AccountDao {
        return database.accountDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideSyncOperationDao(database: AppDatabase): SyncOperationDao {
        return database.syncOperationDao()
    }

}
