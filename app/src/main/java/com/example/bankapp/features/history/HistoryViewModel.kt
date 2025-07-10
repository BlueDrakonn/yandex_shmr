package com.example.bankapp.features.history


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import com.example.bankapp.features.common.extensions.filterExpenses
import com.example.bankapp.features.common.extensions.filterIncome
import com.example.bankapp.features.history.models.DateMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class HistoryViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val historyTransactionRepository: HistoryTransactionRepository
) : ViewModel() {

    private var historyTransactionResponseJob: Job? = null
    private var isIncomeHistory = false

    private val _startDate =
        MutableStateFlow(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE))
    val startDate = _startDate

    private val _endDate = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
    val endDate = _endDate

    private val _transactionState = MutableStateFlow<ResultState<List<TransactionDetailed>>>(
        ResultState.Loading
    )
    val transactionState = _transactionState

    private var _totalAmountState = MutableStateFlow<Double>(0.0)
    val totalAmountState = _totalAmountState


    fun defaultDate() {
        viewModelScope.launch {
            _startDate.value = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE)
            _endDate.value = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

        }
    }

    private fun startGettingHistoryTransactions() {

        if (historyTransactionResponseJob?.isActive == true) {
            return
        }
        historyTransactionResponseJob = viewModelScope.launch(Dispatchers.IO) {

            val accountId = accountRepository.accountId ?: run {
                accountRepository.loadAccounts()
                accountRepository.accountId
            }

            if (accountId == null) {
                _transactionState.value = ResultState.Error(accountRepository.accountError)
            } else {
                val result = historyTransactionRepository.loadHistoryTransaction(
                    accountId = accountId,
                    startDate = _startDate.value,
                    endDate = _endDate.value,
                ).let { data ->
                    if (isIncomeHistory) data.filterIncome() else data.filterExpenses()
                }
                when (result) {
                    is ResultState.Success -> {
                        _totalAmountState.value = result.data.sumOf { it.amount.toDouble() }
                    }

                    else -> {}
                }
                _transactionState.value = result
            }


        }
    }

    fun cancelGettingHistoryTransactions() {
        historyTransactionResponseJob?.cancel()
    }

    fun updateDate(mode: DateMode, newDate: String) {
        when (mode) {
            DateMode.START -> {
                _startDate.value = newDate
            }

            DateMode.END -> {
                _endDate.value = newDate
            }
        }
        if (_startDate.value <= _endDate.value) {
            cancelGettingHistoryTransactions()
            startGettingHistoryTransactions()
        }
    }

    fun setHistoryType(isIncome: Boolean) {
        isIncomeHistory = isIncome
        cancelGettingHistoryTransactions()
        startGettingHistoryTransactions()
    }
}