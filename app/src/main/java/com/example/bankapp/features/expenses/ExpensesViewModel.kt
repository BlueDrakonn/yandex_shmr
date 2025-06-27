package com.example.bankapp.features.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.domain.model.IncomeInfo
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.TodayTransactionRepository
import com.example.bankapp.core.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val transactionRepository: TodayTransactionRepository,
    private val accountRepository: AccountRepository
): ViewModel() {

    private val _transactionState = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    val transactionState= _transactionState
        .onStart {
            loadExpensesTransactions()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ResultState.Loading)

    private var _totalExpensesState = MutableStateFlow<Double>(0.0)
    val totalExpensesState = _totalExpensesState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            0.0)

    private fun loadExpensesTransactions(){
        viewModelScope.launch {
            val accountId = accountRepository.accountId ?: run {
                accountRepository.loadAccounts()
                accountRepository.accountId
            }

            val result = transactionRepository.loadTodayTransaction(
                accountId=accountId
            ).filterExpenses()
            when(result) {
                is ResultState.Success -> {
                    _totalExpensesState.value = result.data.sumOf { it.amount.toDouble() }
                }
                else -> {}
            }
            _transactionState.value = result

        }

    }
}

fun<T: IncomeInfo> ResultState<List<T>>.filterExpenses(): ResultState<List<T>> =

    when (this) {
        is ResultState.Success -> ResultState.Success(this.data.filter { !it.isIncome })
        else -> this
    }