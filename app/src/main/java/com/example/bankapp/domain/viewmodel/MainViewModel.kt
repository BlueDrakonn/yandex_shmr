package com.example.bankapp.domain.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.data.model.toTransaction
import com.example.bankapp.data.model.toTransactionDetailed
import com.example.bankapp.data.network.retrofit.RetrofitInstance
import com.example.bankapp.data.repository.MainRepositoryImpl
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.MainRepository
import com.google.common.base.CharMatcher.`is`
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter



sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String?, val code: Int? = null, val throwable: Throwable? = null) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}

class MainViewModel(): ViewModel() {

    private var job: Job? = null


    //исправить
    private val repository: MainRepository = MainRepositoryImpl(
        apiService = RetrofitInstance.api
    )

    private var accountId: Int? = null

    private val _categoryState = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categoryState: StateFlow<ResultState<List<Category>>> = _categoryState

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense

    private val _totalIncomeHistory = MutableStateFlow(0.0)
    val totalIncomeHistory: StateFlow<Double> = _totalIncomeHistory

    private val _totalExpenseHistory = MutableStateFlow(0.0)
    val totalExpenseHistory: StateFlow<Double> = _totalExpenseHistory

    private val _accounts = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    val accounts: StateFlow<ResultState<List<Account>>> = _accounts

    private val _incomeTransactionState = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    val incomeTransactionState: StateFlow<ResultState<List<Transaction>>> = _incomeTransactionState

    private val _expenseTransactionState = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    val expenseTransactionState: StateFlow<ResultState<List<Transaction>>> = _expenseTransactionState

    private val _incomeTransactionHistoryState = MutableStateFlow<ResultState<List<TransactionDetailed>>>(ResultState.Loading)
    val incomeTransactionHistoryState: StateFlow<ResultState<List<TransactionDetailed>>> = _incomeTransactionHistoryState

    private val _expenseTransactionHistoryState = MutableStateFlow<ResultState<List<TransactionDetailed>>>(ResultState.Loading)
    val expenseTransactionHistoryState: StateFlow<ResultState<List<TransactionDetailed>>> = _expenseTransactionHistoryState


    //пока так тк все равно выбор даты не сделан
    private val _startDate = MutableStateFlow<String>(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE))
    val startDate: StateFlow<String> = _startDate
    private val _endDate = MutableStateFlow<String>(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
    val endDate: StateFlow<String> = _endDate

    fun defaultDate() {
        viewModelScope.launch {
            _startDate.value = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE)
            _endDate.value = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        }

    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    Log.d("COROUTINE", Thread.currentThread().name)
                    getAccounts()
                    accountId?.let { getTodayTransactions() }
                }
                launch {
                    Log.d("COROUTINE", Thread.currentThread().name)
                    getCategories()
                }

            }



        }
    }


    private suspend fun getTransactions(){

        val result = accountId?.let {

                repository.getTransactions(
                    accountId = it,
                    startDate = startDate.value,
                    endDate = endDate.value
                )


        }
        when(result) {
            is ResultState.Success -> {
                val incomeList = mutableListOf<TransactionDetailed>()
                val expenseList = mutableListOf<TransactionDetailed>()

                result.data.forEach{ transaction ->
                    val domainModel = transaction.toTransactionDetailed()
                    if (transaction.category.isIncome) {
                        incomeList.add(domainModel)
                    } else {
                        expenseList.add(domainModel)
                    }

                }

                _incomeTransactionHistoryState.value = ResultState.Success(incomeList)
                _expenseTransactionHistoryState.value = ResultState.Success(expenseList)

                _totalIncomeHistory.value = incomeList.sumOf { it.amount.toDouble() }
                _totalExpenseHistory.value = expenseList.sumOf { it.amount.toDouble() }

            }
            is ResultState.Error -> {
                _incomeTransactionHistoryState.value = result
                _expenseTransactionHistoryState.value = result
            }
            else -> Unit
        }
    }

    private suspend fun getTodayTransactions(){

        if(
            _expenseTransactionHistoryState.value !is ResultState.Loading &&
            _incomeTransactionHistoryState.value !is ResultState.Loading) return

        val result = accountId?.let {

                repository.getTodayTransactions(
                    accountId = it,
                )


        }
        when(result) {
            is ResultState.Success -> {
                val incomeList = mutableListOf<Transaction>()
                val expenseList = mutableListOf<Transaction>()

                result.data.forEach{ transaction ->
                    val domainModel = transaction.toTransaction()
                    if (transaction.category.isIncome) {
                        incomeList.add(domainModel)
                    } else {
                        expenseList.add(domainModel)
                    }

                }

                _incomeTransactionState.value = ResultState.Success(incomeList)
                _expenseTransactionState.value = ResultState.Success(expenseList)

                _totalIncome.value = incomeList.sumOf { it.amount.toDouble()   }
                _totalExpense.value = expenseList.sumOf { it.amount.toDouble() }

            }
            is ResultState.Error -> {
                _incomeTransactionState.value = result
                _expenseTransactionState.value = result
            }
            else -> Unit
        }
    }

    private suspend fun getCategories() {
        _categoryState.value =  repository.getCategories()
    }

    private suspend fun getAccounts(){

        val result =  repository.getAccounts()

        when(result) {
            is ResultState.Success -> {
                _accounts.value = result
                accountId = result.data.firstOrNull()?.id

            }
            is ResultState.Error -> {
                val errorMessage = "не удалось получить номер счета"
                _accounts.value = result
                _expenseTransactionState.value = ResultState.Error(message = errorMessage)
                _incomeTransactionState.value = ResultState.Error(message = errorMessage)
                _expenseTransactionHistoryState.value = ResultState.Error(message = errorMessage)
                _incomeTransactionHistoryState.value = ResultState.Error(message = errorMessage)
            }
            else -> Unit
        }
    }

    fun startGettingHistoryTransactions() {

        //if (incomeTransactionHistoryState.value !is ResultState.Loading &&
            //expenseTransactionHistoryState.value !is ResultState.Loading) return

        if (job?.isActive == true) return

        job = viewModelScope.launch {
            withContext(Dispatchers.IO){
                //Log.d("COROUTINE", Thread.currentThread().name)
                getTransactions()
            }

        }
    }
    fun cancelGettingHistoryTransactions() {
        job?.cancel()
    }

    fun changeStartDate(date : Long?) {
        val isoDate = convertMillisToIsoDate(date)
        isoDate?.let {

            _startDate.value = isoDate
        }

    }

    fun changeEndDate(date : Long?) {
        val isoDate = convertMillisToIsoDate(date)

        isoDate?.let {
            _endDate.value = isoDate
        }
    }
}


fun convertMillisToIsoDate(dateMillis: Long?): String? {
    return dateMillis?.let {
        val date = Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        date.format(DateTimeFormatter.ISO_DATE) // формат yyyy-MM-dd
    }
}