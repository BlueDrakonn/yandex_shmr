package com.example.bankapp.data.repository


import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category

import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.viewmodel.ResultState
import com.example.bankapp.utils.safeApiCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AccountRepositoryImpl(private val apiService: ApiService): AccountRepository {

    override var accountId: Int? = null


    private val _accountState = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    override val accountState: StateFlow<ResultState<List<Account>>> = _accountState

    override suspend fun loadAccounts() {
        when (_accountState.value) {
            ResultState.Loading -> {
                var result = safeApiCall(
                    mapper = { it.toAccount() },
                    block = { apiService.getAccounts()  }
                )


                if(result == ResultState.Error(message = "no internet connection")) {
                    _accountState.value = result
                    delay(7000)
                    result = safeApiCall(
                        mapper = { it.toAccount() },
                        block = { apiService.getAccounts()  }
                    )
                }


                when(result) {

                    is ResultState.Success -> {
                        //result = result.data.map { it.toAccount() }
                        _accountState.value = ResultState.Success(result.data)
                        accountId = result.data.firstOrNull()?.userId
                    }
                    else -> _accountState.value = result
                }

            }
            else -> {}
        }
    }


}