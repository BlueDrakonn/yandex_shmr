package com.example.bankapp.data.repository


import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.TodayTransactionRepository
import com.example.bankapp.domain.viewmodel.ResultState
import com.example.bankapp.utils.safeApiCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayTransactionRepositoryImpl(private val apiService: ApiService): TodayTransactionRepository {

    private val _transactionState = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    override val transactionState: StateFlow<ResultState<List<Transaction>>> = _transactionState


    private var _totalExpensesState = MutableStateFlow<Double>(0.0)
    override val totalExpensesState = _totalExpensesState

    private var _totalIncomeState = MutableStateFlow<Double>(0.0)
    override val totalIncomeState = _totalIncomeState





    override suspend fun loadTodayTransaction(accountId: Int?) {


        if(accountId == null) _transactionState.value = ResultState.Error(message = "не удалось получить id счета")

        when(_transactionState.value) {



            ResultState.Loading -> {
                var result = safeApiCall(
                    mapper = {
                        val transaction = it.toTransaction()
                        if (transaction.isIncome) {
                            _totalIncomeState.value +=  transaction.amount.toDouble()
                        }  else{
                            _totalExpensesState.value +=  transaction.amount.toDouble()
                        }
                        transaction
                             },
                    block = {
                        apiService.getTransactions(
                            accountId = accountId!!,
                            startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                            endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                        )
                        }
                )

                if(result == ResultState.Error(message = "no internet connection")) {
                    delay(7000)
                    _transactionState.value = result

                     result = safeApiCall(
                        mapper = {
                            val transaction = it.toTransaction()
                            if (transaction.isIncome) {
                                _totalIncomeState.value +=  transaction.amount.toDouble()
                            }  else{
                                _totalExpensesState.value +=  transaction.amount.toDouble()
                            }
                            transaction
                        },
                        block = {apiService.getTransactions(
                            accountId = accountId!!,
                            startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                            endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                        )
                        }
                    )
                }
                when(result) {

                    is ResultState.Success -> {
                        _transactionState.value = ResultState.Success(result.data)

                    }

                    else -> {
                        _transactionState.value = result
                    }
                }

            }
            else -> {}
        }


    }


}