package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.data.model.TransactionResponseDto

interface MockInterface {
    suspend fun getAccounts(): List<Account>
    suspend fun getCategories(): List<Category>
    suspend fun getTodayTransactionsByAccount(
        accountId: Int,
    ): List<TransactionResponseDto>
}
