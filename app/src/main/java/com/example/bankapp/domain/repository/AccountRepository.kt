package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.viewmodel.ResultState
import kotlinx.coroutines.flow.StateFlow

interface AccountRepository {
    var accountId:  Int?
    val accountState: StateFlow<ResultState<List<Account>>>
    suspend fun loadAccounts()
}