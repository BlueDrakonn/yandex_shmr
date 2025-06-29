package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Account


interface AccountRepository {

    var accountId:  Int?
    var accountCurrency: String?
    var accountError: String?
    suspend fun loadAccounts(): ResultState<List<Account>>
}