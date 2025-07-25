package com.example.bankapp.features.account.account

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
import java.time.OffsetDateTime
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _accountState = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    val accountState = _accountState
        .onStart {
            loadAccounts()
            loadTransactionForChart()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(Delays.STOP_TIMEOUT_MILES),
            ResultState.Loading
        )

    private val _chartState = MutableStateFlow<ResultState<DoubleArray>>(ResultState.Loading)
    val chartState = _chartState

    private fun loadAccounts() {
        viewModelScope.launch {
            _accountState.value = accountRepository.loadAccounts()
        }
    }

    private fun loadTransactionForChart() {
        viewModelScope.launch {
            val result = accountRepository.loadTransactionsForChart()
            val monthlyTotals = DoubleArray(12)
            when (result) {
                is ResultState.Success -> {
                    for (transaction in result.data) {
                        try {
                            val dateTime = OffsetDateTime.parse(transaction.transactionDate)
                            val monthIndex = dateTime.monthValue - 1

                            val amount = transaction.amount.replace(",", ".").toDoubleOrNull() ?: continue

                            if (transaction.isIncome) {
                                monthlyTotals[monthIndex] += amount
                            } else {
                                monthlyTotals[monthIndex] -= amount
                            }
                        } catch (e: Exception) {
                            continue
                        }
                    }
                    _chartState.value = ResultState.Success(data = monthlyTotals)
                }

                else -> { _chartState.value = ResultState.Success(data = monthlyTotals)}
            }

        }
    }


}