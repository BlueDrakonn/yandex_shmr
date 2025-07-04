package com.example.bankapp.features.account.accountEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.Constants.Delays
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateAccountRequest
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.features.account.accountEdit.models.AccountEditIntent
import com.example.bankapp.features.account.accountEdit.utils.isValidNumberInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(
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


    suspend fun handleIntent(intent: AccountEditIntent): ResultState<Account> {
        return when (intent) {
            is AccountEditIntent.OnAccountUpdate -> {

                if (accountRepository.accountId == null) ResultState.Error(message = accountRepository.accountError)

                if (!isValidNumberInput(intent.balance)) ResultState.Error(message = accountRepository.accountError)

                accountUpdate(
                    name = intent.name,
                    balance = intent.balance,
                    currency = intent.currency,
                )


            }


        }
    }


    private suspend fun accountUpdate(
        name: String,
        balance: String,
        currency: String
    ): ResultState<Account> {

        return accountRepository.updateAccount(
            UpdateAccountRequest(
                name = name,
                balance = balance,
                currency = currency
            )
        )
    }


}