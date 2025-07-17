package com.example.bankapp.data.local.repository

import com.example.bankapp.data.local.dao.SyncOperationDao
import com.example.bankapp.data.local.entity.OperationType
import com.example.bankapp.data.local.entity.SyncOperationEntity
import com.example.bankapp.domain.repository.SyncOperationRepository
import javax.inject.Inject

class SyncOperationRepositoryImpl @Inject constructor(
    private val syncOperationDao: SyncOperationDao
): SyncOperationRepository {
    override suspend fun getPendingOperationsByType(type: OperationType, targetId: Int): SyncOperationEntity? {
        return syncOperationDao.findExistingOperation(type=type, targetId = targetId)
    }

    override suspend fun removeOperation(type: List<OperationType>, targetId: Int) {
        syncOperationDao.deleteTransactionOperations(
            transactionId = targetId,
            types = type
        )
    }


}