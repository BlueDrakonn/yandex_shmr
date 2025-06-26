package com.example.bankapp.data.repository


import android.util.Log
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import com.example.bankapp.domain.viewmodel.ResultState
import com.example.bankapp.utils.safeApiCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoryTransactionRepositoryImpl(private val apiService: ApiService): HistoryTransactionRepository {

    private val _startDate = MutableStateFlow(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE))
    override val startDate = _startDate

    private val _endDate = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
    override val endDate = _endDate


    private var lastResponseStartDate: String? = null
    private var lastResponseEndDate: String? = null

    private val _transactionState = MutableStateFlow<ResultState<List<TransactionDetailed>>>(ResultState.Loading)
    override val transactionState = _transactionState


    private var _totalExpensesState = MutableStateFlow<Double>(0.0)
    override val totalExpensesState = _totalExpensesState

    private var _totalIncomeState = MutableStateFlow<Double>(0.0)
    override val totalIncomeState = _totalIncomeState

    override suspend fun loadHistoryTransaction(accountId: Int?) {

        if(accountId == null) _transactionState.value = ResultState.Error(message = "не удалось получить id счета")

        when(_transactionState.value) {

            is ResultState.Loading -> {
                loadTransaction(accountId = accountId!!)
            }
            is ResultState.Success -> {
                if (_startDate.value > _endDate.value) return //для случаев когда пользователь меняет дату и дата начала дальше даты конца

                if(_startDate.value == lastResponseStartDate && _endDate.value == lastResponseEndDate) return

                _totalIncomeState.value = 0.0
                _totalExpensesState.value = 0.0
                loadTransaction(accountId = accountId!!)
            }
            else -> {}
        }
    }

    override fun setStartDate(value: String) {

        _startDate.value = value
    }

    override fun setEndDate(value: String) {
        _endDate.value = value
    }


    private suspend fun loadTransaction(accountId: Int) {

                var result = safeApiCall(
                    mapper = {
                        val transaction = it.toTransactionDetailed()
                        if (transaction.isIncome) {
                            _totalIncomeState.value +=  transaction.amount.toDouble()
                        }  else{
                            _totalExpensesState.value +=  transaction.amount.toDouble()
                        }
                        transaction
                    },
                    block = {apiService.getTransactions(
                        accountId = accountId,
                        startDate = _startDate.value,
                        endDate = _endDate.value
                    )
                    }
                )


                if(result == ResultState.Error(message = "no internet connection")) {
                    _transactionState.value = result
                    delay(7000)

                    result = safeApiCall(
                        mapper = {
                            val transaction = it.toTransactionDetailed()
                            if (transaction.isIncome) {
                                _totalIncomeState.value +=  transaction.amount.toDouble()
                            }  else{
                                _totalExpensesState.value +=  transaction.amount.toDouble()
                            }
                            transaction
                        },
                        block = {apiService.getTransactions(
                            accountId = accountId,
                            startDate = _startDate.value,
                            endDate = _endDate.value
                        )
                        }
                    )
                    Log.d("ERROR_500","$result")



                }



                when(result) {

                    is ResultState.Success -> {
                        lastResponseStartDate = _startDate.value //запоминаем даты сделаного запроса, чтобы не повторять запрос при возвращение на экран истории с той же датой
                        lastResponseEndDate = _endDate.value
                        _transactionState.value = ResultState.Success(result.data.sortedBy{it.transactionDate})

                    }
                    else -> {
                        _transactionState.value = result
                    }
                }
    }

}


