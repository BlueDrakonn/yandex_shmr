package com.example.bankapp.domain.repository

import com.example.bankapp.data.model.TransactionResponseDto
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.viewmodel.ResultState


interface MainRepository {

    suspend fun getTransactions(
        accountId: Int,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionResponseDto>>

    suspend fun getCategories(): ResultState<List<Category>>

    suspend fun getAccounts(): ResultState<List<Account>>

    suspend fun getTodayTransactions(
        accountId: Int,
    ): ResultState<List<TransactionResponseDto>>


}