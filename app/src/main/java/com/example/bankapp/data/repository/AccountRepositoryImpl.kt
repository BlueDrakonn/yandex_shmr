package com.example.bankapp.data.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.utils.safeApiCall
import javax.inject.Inject


class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): AccountRepository {

    override var accountId: Int? = null

    override suspend fun loadAccounts(): ResultState<List<Account>> {
        val result = safeApiCall(
            mapper = { it.toAccount() },
            block = { apiService.getAccounts()  }
        )
        when(result) {
            is ResultState.Success -> {
                accountId = result.data.firstOrNull()?.userId
                return result
            }
            else -> return result
        }
    }
}