package com.example.bankapp.domain.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.data.model.TransactionResponse
import com.example.bankapp.mock.repository.MockRepository
import com.example.bankapp.domain.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    object Loading : ResultState<Nothing>()
    data class Error(val message: String? = null, val throwable: Throwable? = null) : ResultState<Nothing>()
}


class MainViewModel : ViewModel() {

    private val repository = MockRepository() // Пока mock

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense

    private val _categories = MutableStateFlow<ResultState<List<Category>>>(ResultState.Loading)
    val categories: StateFlow<ResultState<List<Category>>> = _categories

    private val _accounts = MutableStateFlow<ResultState<List<Account>>>(ResultState.Loading)
    val accounts: StateFlow<ResultState<List<Account>>> = _accounts

    private val _incomeTransactions = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    val incomeTransactions: StateFlow<ResultState<List<Transaction>>> = _incomeTransactions

    private val _expenseTransactions = MutableStateFlow<ResultState<List<Transaction>>>(ResultState.Loading)
    val expenseTransactions: StateFlow<ResultState<List<Transaction>>> = _expenseTransactions

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {

            try {
                val loadedCategories = repository.getCategories()
                _categories.value = ResultState.Success(loadedCategories)
            } catch (e: Exception) {
                _categories.value = ResultState.Error(e.message, e)
            }

            try {
                val loadedAccounts = repository.getAccounts()
                _accounts.value = ResultState.Success(loadedAccounts)

                val firstAccountId = loadedAccounts.firstOrNull()?.id
                if (firstAccountId != null) {
                    loadTransactionsForAccount(firstAccountId)
                } else {
                    _incomeTransactions.value = ResultState.Success(emptyList())
                    _expenseTransactions.value = ResultState.Success(emptyList())
                }
            } catch (e: Exception) {
                _accounts.value = ResultState.Error(e.message, e)
                _incomeTransactions.value = ResultState.Error("Не найдено транзакций", null)
                _expenseTransactions.value = ResultState.Error("Не найдено транзакций", null)
            }
        }
    }

    private suspend fun loadTransactionsForAccount(accountId: Int) {
        _incomeTransactions.value = ResultState.Loading
        _expenseTransactions.value = ResultState.Loading

        try {
            val todayTransactions = repository.getTodayTransactionsByAccount(accountId)
            val incomeList = mutableListOf<Transaction>()
            val expenseList = mutableListOf<Transaction>()

            todayTransactions.forEach { transaction ->
                val uiModel = transaction.toUi()
                if (transaction.category.isIncome) {
                    incomeList.add(uiModel)
                } else {
                    expenseList.add(uiModel)
                }
            }

            _incomeTransactions.value = ResultState.Success(incomeList)
            _expenseTransactions.value = ResultState.Success(expenseList)

            _totalIncome.value = incomeList.sumOf { it.amount }
            _totalExpense.value = expenseList.sumOf { it.amount }

        } catch (e: Exception) {
            _incomeTransactions.value = ResultState.Error(e.message, e)
            _expenseTransactions.value = ResultState.Error(e.message, e)
            _totalIncome.value = 0.0
            _totalExpense.value = 0.0
        }
    }
}

fun TransactionResponse.toUi(): Transaction {
    return Transaction(
        id = id,
        title = category.title,
        subtitle = comment,
        icon = category.icon,
        amount = amount,
        currency = account.currency
    )
}