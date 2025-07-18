package com.example.bankapp.domain.repository

import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.SyncOperationEntity

interface SyncOperationRepository {
    suspend fun getPendingOperations(): List<SyncOperationEntity>
    suspend fun getPendingOperationsByType(type: OperationType, targetId: Int): SyncOperationEntity?
    suspend fun removeOperation(type: List<OperationType>, targetId: Int)
}
