package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Account


interface AccountRepository {

    var accountId:  Int?

    suspend fun loadAccounts(): ResultState<List<Account>>
}