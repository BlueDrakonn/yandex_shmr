package com.example.bankapp.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {

    private val _accountState = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    val accountState = _accountState
        .onStart {
            loadAccounts()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ResultState.Loading
            )

    private fun loadAccounts() {
        viewModelScope.launch {
            _accountState.value = accountRepository.loadAccounts()
        }
    }



}