package com.example.bankapp.features.account.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.Constants.Delays
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    init{
        Log.d("INIT_ACCOUNT_VIEW_Model","start")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("INIT_ACCOUNT_VIEW_Model","end")
    }

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


}