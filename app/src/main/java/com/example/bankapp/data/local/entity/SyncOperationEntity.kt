package com.example.bankapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_operations")
data class SyncOperationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: OperationType, // ADD_TRANSACTION, UPDATE_ACCOUNT и т.п.
    val payload: String, // JSON
    val createdAt: String,
    val targetId: Int? = null
)

enum class OperationType {
    ADD_TRANSACTION,
    UPDATE_TRANSACTION,
    DELETE_TRANSACTION,
    UPDATE_ACCOUNT
}