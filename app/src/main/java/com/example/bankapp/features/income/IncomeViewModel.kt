package com.example.bankapp.features.income


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.TodayTransactionRepository
import com.example.bankapp.core.ResultState
import com.example.bankapp.features.common.extensions.filterIncome
import com.example.bankapp.core.Constants.Delays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val transactionRepository: TodayTransactionRepository,
    private val accountRepository: AccountRepository
): ViewModel()  {

    private val _transactionState = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    val transactionState= _transactionState
        .onStart {
            loadIncomeTransactions()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(Delays.STOP_TIMEOUT_MILES),
            ResultState.Loading)

    private var _totalIncomeState = MutableStateFlow<Double>(0.0)
    val totalIncomeState = _totalIncomeState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(Delays.STOP_TIMEOUT_MILES),
            0.0)

    private fun loadIncomeTransactions(){
        viewModelScope.launch {
            val accountId = accountRepository.accountId ?: run {
                accountRepository.loadAccounts()
                accountRepository.accountId
            }

            val result = transactionRepository.loadTodayTransaction(
                accountId=accountId
            ).filterIncome()
            when(result) {
                is ResultState.Success -> {
                    _totalIncomeState.value = result.data.sumOf { it.amount.toDouble() }
                }
                else -> {}
            }
            _transactionState.value = result

        }

    }

    fun currentCurrency(): String {
        return accountRepository.accountCurrency ?: "₽"
    }
}




