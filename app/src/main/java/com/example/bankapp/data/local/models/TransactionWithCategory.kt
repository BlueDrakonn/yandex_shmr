package com.example.bankapp.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.bankapp.data.local.entity.CategoryEntity
import com.example.bankapp.data.local.entity.TransactionEntity

data class TransactionWithCategory(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)