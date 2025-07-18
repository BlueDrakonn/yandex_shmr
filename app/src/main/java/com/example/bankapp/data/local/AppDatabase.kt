package com.example.bankapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bankapp.data.local.dao.AccountDao
import com.example.bankapp.data.local.dao.CategoryDao
import com.example.bankapp.data.local.dao.SyncOperationDao
import com.example.bankapp.data.local.dao.TransactionDao
import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.local.entity.CategoryEntity
import com.example.bankapp.data.local.entity.AccountEntity
import com.example.bankapp.data.local.entity.SyncOperationEntity


@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        SyncOperationEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun syncOperationDao(): SyncOperationDao
}
