package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.model.UpdateAccountRequest
import com.example.bankapp.domain.model.Account


interface AccountRepository {

    var accountId: Int?
    var accountCurrency: String?
    var accountError: String?

    suspend fun loadAccounts(): ResultState<List<Account>>

    suspend fun updateAccount(
        request: UpdateAccountRequest
    ): ResultState<Account>
}