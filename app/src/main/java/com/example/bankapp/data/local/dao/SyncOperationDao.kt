package com.example.bankapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.SyncOperationEntity

@Dao
interface SyncOperationDao {

    @Query("SELECT * FROM sync_operations")
    suspend fun getPendingOperations(): List<SyncOperationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: SyncOperationEntity)

    @Delete
    suspend fun deleteOperation(operation: SyncOperationEntity)

    @Query(
        """
    SELECT * FROM sync_operations 
    WHERE type = :type AND targetId = :targetId
    LIMIT 1
"""
    )
    suspend fun findExistingOperation(type: OperationType, targetId: Int): SyncOperationEntity?

    @Query("""
    DELETE FROM sync_operations
    WHERE targetId = :transactionId AND type IN (:types)
""")
    suspend fun deleteTransactionOperations(transactionId: Int, types: List<OperationType>)



}
