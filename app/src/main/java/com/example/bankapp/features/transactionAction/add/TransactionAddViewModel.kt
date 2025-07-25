package com.example.bankapp.features.transactionAction.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.model.UpdateTransactionRequest
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.features.account.accountEdit.utils.isValidNumberInput
import com.example.bankapp.features.transactionAction.add.models.TransactionAddIntent
import com.example.bankapp.features.transactionAction.add.models.TransactionFormState
import com.example.bankapp.features.transactionAction.models.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class TransactionAddViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionActionRepository: TransactionActionRepository
) : ViewModel() {


    private val _state = MutableStateFlow<ResultState<TransactionFormState>>(ResultState.Loading)
    val state = _state

    private val _requestState = MutableStateFlow<RequestState>(RequestState.Idle)
    val requestState = _requestState

    fun requestDialogDismiss() {
        _requestState.value = RequestState.Idle
    }

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
                                        "yyyy-MM-dd",
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


    fun addTransaction(

    ) {

        val currentState = _state.value

        when (currentState) {
            is ResultState.Success -> {

                if (accountRepository.accountId == null) {
                    _requestState.value =
                        RequestState.Error(accountRepository.accountError ?: "Неизвестная ошибка")
                    return
                }

                if (!isValidNumberInput(currentState.data.amount)) {
                    _requestState.value = RequestState.Error("неверный формат баланса")
                    return
                }

                _requestState.value = RequestState.Loading

                viewModelScope.launch(Dispatchers.IO) {

                    val result = transactionActionRepository.addTransaction(

                        request = UpdateTransactionRequest(
                            accountId = accountRepository.accountId!!,
                            categoryId = currentState.data.selectedCategory.id,
                            amount = currentState.data.amount,
                            transactionDate = "${currentState.data.date}T${currentState.data.time}:00.000Z",
                            comment = if (currentState.data.comment == "") null else currentState.data.comment
                        )
                    )
                    when (result) {
                        is ResultState.Error -> {
                            _requestState.value =
                                RequestState.Error(result.message ?: "Неизвестная ошибка")
                        }

                        else -> {
                            _requestState.value = RequestState.Success
                        }
                    }
                }


            }

            is ResultState.Error -> {
                _requestState.value =
                    RequestState.Error(message = currentState.message ?: "Неизвестная ошибка")

            }

            else -> {}
        }


    }

}

