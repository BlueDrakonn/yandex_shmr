package com.example.bankapp.features.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.TransactionRepository
import com.example.bankapp.features.analysis.models.AnalysisFormState
import com.example.bankapp.features.analysis.models.AnalysisIntent
import com.example.bankapp.features.analysis.models.utils.toUiModel
import com.example.bankapp.features.common.extensions.filterExpenses
import com.example.bankapp.features.common.extensions.filterIncome
import com.example.bankapp.features.history.models.DateMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

class AnalysisViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private var isIncome = false
    private var analysisTransactionResponseJob: Job? = null


    private val _analysisFormState =
        MutableStateFlow(AnalysisFormState())
    val analysisFormState = _analysisFormState


    fun getCurrency(): String{
        return accountRepository.accountCurrency ?: ""
    }

    private fun startGettingHistoryTransactions() {

        if (analysisTransactionResponseJob?.isActive == true) {
            return
        }
        analysisTransactionResponseJob = viewModelScope.launch(Dispatchers.IO) {

            val accountId = accountRepository.accountId ?: run {
                accountRepository.loadAccounts()
                accountRepository.accountId
            }

            if (accountId == null) {

                _analysisFormState.value = _analysisFormState.value.copy(
                    transactionRequestState = ResultState.Error(message = accountRepository.accountError)
                )

            } else {
                val result = transactionRepository.loadHistoryTransaction(
                    accountId = accountId,
                    startDate = analysisFormState.value.startDate,
                    endDate = analysisFormState.value.endDate,
                ).let { data ->
                    if (isIncome) data.filterIncome() else data.filterExpenses()
                }
                when (result) {
                    is ResultState.Success -> {

                        _analysisFormState.value = _analysisFormState.value.copy(
                            totalAmount =
                                result.data.sumOf { it.amount.toDouble() })

                        _analysisFormState.value =
                            _analysisFormState.value.copy(
                                transactionRequestState =
                                ResultState.Success(data = result.data.sortedByDescending { transaction ->
                                    Instant.parse(transaction.transactionDate)
                                }.toUiModel())
                            )
                    }

                    is ResultState.Error -> {
                        _analysisFormState.value = _analysisFormState.value.copy(
                            transactionRequestState =
                                ResultState.Error(message = result.message, code = result.code)
                        )
                    }

                    is ResultState.Loading -> {}
                }

            }


        }
    }


    fun analysisIntentHandler(analysisIntent: AnalysisIntent) {
        when (analysisIntent) {
            AnalysisIntent.onDismiss -> {
                cancelGettingHistoryTransactions()
            }

            is AnalysisIntent.onUpdateDate -> {
                updateDate(
                    mode = analysisIntent.mode,
                    newDate = analysisIntent.date
                )
            }

            is AnalysisIntent.setAnalysisType -> {
                setAnalysisType(analysisIntent.analysisType)
            }
        }
    }

    private fun cancelGettingHistoryTransactions() {
        analysisTransactionResponseJob?.cancel()
    }


    private fun setAnalysisType(analysisType: Boolean) {
        isIncome = analysisType
        cancelGettingHistoryTransactions()
        startGettingHistoryTransactions()
    }

    private fun updateDate(mode: DateMode, newDate: String) {
        when (mode) {
            DateMode.START -> {
                _analysisFormState.value = _analysisFormState.value.copy(startDate = newDate)
            }

            DateMode.END -> {
                _analysisFormState.value = _analysisFormState.value.copy(endDate = newDate)
            }
        }
        if (_analysisFormState.value.startDate <= _analysisFormState.value.endDate) {
            cancelGettingHistoryTransactions()
            startGettingHistoryTransactions()
        }
    }


}