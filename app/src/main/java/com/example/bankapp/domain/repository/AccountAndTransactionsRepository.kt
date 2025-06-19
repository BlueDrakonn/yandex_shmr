package com.example.bankapp.domain.repository

import com.example.bankapp.data.model.TransactionResponseDto
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.viewmodel.ResultState

interface AccountAndTransactionsRepository {

    suspend fun getAccounts(): ResultState<List<Account>>

    suspend fun getTodayTransactions(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ):ResultState<List<TransactionResponseDto>>
}