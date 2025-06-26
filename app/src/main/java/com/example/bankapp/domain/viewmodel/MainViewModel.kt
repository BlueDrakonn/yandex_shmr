package com.example.bankapp.domain.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.data.network.retrofit.RetrofitInstance
import com.example.bankapp.data.repository.AccountRepositoryImpl
import com.example.bankapp.data.repository.CategoryRepositoryImpl
import com.example.bankapp.data.repository.HistoryTransactionRepositoryImpl
import com.example.bankapp.data.repository.TodayTransactionRepositoryImpl
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.IncomeInfo
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import com.example.bankapp.domain.repository.TodayTransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter



sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String?, val code: Int? = null, val throwable: Throwable? = null) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}

class MainViewModel: ViewModel() {


    private var historyTransactionResponseJob: Job? = null


    private val categoryRepository: CategoryRepository = CategoryRepositoryImpl(
        apiService = RetrofitInstance.api
    )

    private val accountRepository: AccountRepository = AccountRepositoryImpl(
        apiService = RetrofitInstance.api
    )

    private val todayTransactionRepository: TodayTransactionRepository = TodayTransactionRepositoryImpl(
        apiService = RetrofitInstance.api
    )

    private val historyTransactionRepository: HistoryTransactionRepository = HistoryTransactionRepositoryImpl(
        apiService = RetrofitInstance.api
    )

    fun defaultDate() {
        viewModelScope.launch {
            historyTransactionRepository.setStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE))
            historyTransactionRepository.setEndDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))

        }

    }


    fun observeCategories(): StateFlow<ResultState<List<Category>>> {
        return categoryRepository.categoryState
    }

    fun observeAccounts(): StateFlow<ResultState<List<Account>>> {
        return accountRepository.accountState
    }

    fun observeTodayExpenses(): StateFlow<ResultState<List<Transaction>>>{
        return todayTransactionRepository.transactionState.filterExpenses().stateIn(viewModelScope, SharingStarted.Lazily, ResultState.Loading)
    }

    fun observeTodayIncome(): StateFlow<ResultState<List<Transaction>>>{
        return todayTransactionRepository.transactionState.filterIncome().stateIn(viewModelScope, SharingStarted.Lazily, ResultState.Loading)
    }

    fun observeTodayTotalExpenses(): StateFlow<Double>{
        return todayTransactionRepository.totalExpensesState
    }

    fun observeTodayTotalIncome(): StateFlow<Double>{
        return todayTransactionRepository.totalIncomeState
    }

    fun observeHistoryExpenses(): StateFlow<ResultState<List<TransactionDetailed>>>{
        return historyTransactionRepository.transactionState.filterExpenses().stateIn(viewModelScope, SharingStarted.Lazily, ResultState.Loading)
    }

    fun observeHistoryIncome(): StateFlow<ResultState<List<TransactionDetailed>>>{
        return historyTransactionRepository.transactionState.filterIncome().stateIn(viewModelScope, SharingStarted.Lazily, ResultState.Loading)
    }

    fun observeHistoryTotalExpenses(): StateFlow<Double>{
        return historyTransactionRepository.totalExpensesState
    }

    fun observeHistoryTotalIncome(): StateFlow<Double>{
        return historyTransactionRepository.totalIncomeState
    }

    fun observeStartDate(): StateFlow<String>{
        return historyTransactionRepository.startDate
    }

    fun observeEndDate(): StateFlow<String>{
        return historyTransactionRepository.endDate
    }





    init {
        viewModelScope.launch(Dispatchers.IO) {

                launch {
                    accountRepository.loadAccounts()
                    todayTransactionRepository.loadTodayTransaction(accountRepository.accountId)

                }
                launch {
                    categoryRepository.loadCategories()
                }
        }
    }


    fun startGettingHistoryTransactions() {

        if (historyTransactionResponseJob?.isActive == true){

            return
        }

        historyTransactionResponseJob = viewModelScope.launch(Dispatchers.IO) {
            accountRepository.accountId?.let {
                historyTransactionRepository.loadHistoryTransaction(
                    it
                )
            }
        }


    }

    fun cancelGettingHistoryTransactions() {
        historyTransactionResponseJob?.cancel()
    }

    fun changeStartDate(date : String) {
            historyTransactionRepository.setStartDate(date)
            cancelGettingHistoryTransactions()
            startGettingHistoryTransactions()

    }

    fun changeEndDate(date : String) {
            historyTransactionRepository.setEndDate(date)
            cancelGettingHistoryTransactions()
            startGettingHistoryTransactions()

    }
}


fun<T: IncomeInfo> StateFlow<ResultState<List<T>>>.filterExpenses(): Flow<ResultState<List<T>>> =
    this.   map { resultState ->
        when (resultState) {
            is ResultState.Success -> ResultState.Success(resultState.data.filter { !it.isIncome })
            else -> resultState
        }
    }

fun<T: IncomeInfo> StateFlow<ResultState<List<T>>>.filterIncome(): Flow<ResultState<List<T>>> =
    this.   map { resultState ->
        when (resultState) {
            is ResultState.Success -> ResultState.Success(resultState.data.filter { it.isIncome })
            else -> resultState
        }
    }
