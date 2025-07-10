package com.example.bankapp.features.transactionAction.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateTransactionRequest
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.features.account.accountEdit.utils.isValidNumberInput
import com.example.bankapp.features.transactionAction.add.models.TransactionAddIntent
import com.example.bankapp.features.transactionAction.add.models.TransactionFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TransactionAddViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionActionRepository: TransactionActionRepository
) : ViewModel() {


    private val _state = MutableStateFlow<ResultState<TransactionFormState>>(ResultState.Loading)
    val categoryState = _state

    fun currency(): String {
        return accountRepository.accountCurrency ?: "₽"
    }

    fun loadCategories(isIncome: Boolean) {
        when (_state.value) {
            is ResultState.Success -> {}
            else -> {
                viewModelScope.launch {

                    val result = categoryRepository.loadCategoriesByType(isIncome = isIncome)

                    when (result) {
                        is ResultState.Success -> {
                            _state.value = ResultState.Success(
                                TransactionFormState(
                                    categoryList = result.data,
                                    selectedCategory = result.data.firstOrNull()!!,
                                    date = SimpleDateFormat(
                                        "dd-MM-yyyy",
                                        Locale.getDefault()
                                    ).format(
                                        Date()
                                    ),
                                    time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                                        Date()
                                    ),
                                    amount = "0"
                                )
                            )
                        }

                        is ResultState.Error -> {
                            _state.value =
                                ResultState.Error(message = result.message, code = result.code)
                        }

                        is ResultState.Loading -> {}
                    }


                }
            }
        }
    }

    fun handleIntent(intent: TransactionAddIntent) {

        val currentState = _state.value
        if (currentState !is ResultState.Success) return

        when (intent) {


            is TransactionAddIntent.onAmountChanged -> {

                _state.update { ResultState.Success(data = currentState.data.copy(amount = intent.amount)) }
            }

            is TransactionAddIntent.onCommentChanged -> {
                _state.update { ResultState.Success(data = currentState.data.copy(comment = intent.comment)) }
            }

            is TransactionAddIntent.onDateChanged -> {
                _state.update { ResultState.Success(data = currentState.data.copy(date = intent.date)) }
            }

            is TransactionAddIntent.onTimeChanged -> {
                _state.update { ResultState.Success(data = currentState.data.copy(time = intent.time)) }
            }

            is TransactionAddIntent.onCategoryChanged -> {
                _state.update { ResultState.Success(data = currentState.data.copy(selectedCategory = intent.category)) }
            }
        }
    }


    suspend fun addTransaction(

    ): ResultState<TransactionFormState> {

        val currentState = _state.value
        Log.d("ERROR_TRANS","$currentState")

        if (currentState is ResultState.Success) {
            if (accountRepository.accountId == null) return ResultState.Error(message = accountRepository.accountError)

            if (!isValidNumberInput(currentState.data.amount)) return ResultState.Error(message = "некорректный формат баланса")

            val deferred: Deferred<ResultState<Transaction>> =
                viewModelScope.async(Dispatchers.IO) {
                    transactionActionRepository.addTransaction(
                        request = UpdateTransactionRequest(
                            accountId = accountRepository.accountId ?:28 , //исправить!!
                            categoryId = currentState.data.selectedCategory.id,
                            amount = currentState.data.amount,
                            transactionDate = "2025-07-10T11:44:22.776Z",//"${currentState.data.date}T${currentState.data.time}",
                            comment = currentState.data.comment
                        )
                    )
                }
            val result = deferred.await()

            return when (result) {
                is ResultState.Error -> {
                    Log.d("ERROR_TRANS", "${result.message}")
                    ResultState.Error(message = result.message, code = result.code)
                }

                else -> {
                    ResultState.Success(data = currentState.data)
                }
            }

        } else {
            return currentState
        }

    }

}