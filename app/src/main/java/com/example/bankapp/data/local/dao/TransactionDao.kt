package com.example.bankapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.bankapp.data.local.entity.TransactionEntity
import com.example.bankapp.data.local.models.TransactionWithCategory

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Int): TransactionWithCategory?


    @Query("SELECT * FROM transactions WHERE transactionDate BETWEEN :startDate AND :endDate")
    suspend fun getTransactionsBetweenDates(startDate: String, endDate: String): List<TransactionWithCategory>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT MIN(id) FROM transactions WHERE id < 0")
    fun getMinTransactionId(): Int?

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)
}
