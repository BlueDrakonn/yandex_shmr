package com.example.bankapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val userId: Int,
    val name: String,
    val balance: String,
    val currency: String
)