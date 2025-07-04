package com.example.bankapp.features.account

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.MyApplication
import com.example.bankapp.core.Constants.Delays
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateAccountRequest
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.features.account.store.models.AccountIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _accountState = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    val accountState = _accountState
        .onStart {
            loadAccounts()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(Delays.STOP_TIMEOUT_MILES),
            ResultState.Loading
        )

    private fun loadAccounts() {
        viewModelScope.launch {
            _accountState.value = accountRepository.loadAccounts()
        }
    }

    fun handleIntent(intent: AccountIntent) {
        when (intent) {
            is AccountIntent.OnAccountUpdate -> {
                accountRepository.accountId?.let {
                    accountUpdate(
                        name = intent.name,
                        balance = intent.balance,
                        currency = intent.currency,
                    )
                }
            }


        }
    }


    private fun accountUpdate(name: String, balance: String, currency: String) {

        viewModelScope.launch(Dispatchers.IO) {
            handleUpdateResult(
                accountRepository.updateAccount(
                    UpdateAccountRequest(
                        name = name,
                        balance = balance,
                        currency = currency
                    )
                )
            )
        }
    }

    private fun handleUpdateResult(result: ResultState<Account>) {
        when (result) {
            is ResultState.Success -> {
                accountRepository.accountCurrency = result.data.currency
            }

            is ResultState.Error -> {
                Toast.makeText(
                    MyApplication.context,
                    result.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }


}