package com.example.bankapp.features.transactionAction.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.UpdateTransactionRequest
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionActionRepository
import com.example.bankapp.features.account.accountEdit.utils.isValidNumberInput
import com.example.bankapp.features.transactionAction.add.models.TransactionAddIntent
import com.example.bankapp.features.transactionAction.add.models.TransactionFormState
import com.example.bankapp.features.transactionAction.models.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class TransactionEditViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionActionRepository: TransactionActionRepository
) : ViewModel() {

    private val _formState =
        MutableStateFlow<ResultState<TransactionFormState>>(ResultState.Loading)
    val formState = _formState

    private val _requestState = MutableStateFlow<RequestState>(RequestState.Idle)
    val requestState = _requestState


    fun loadFormState(transactionId: Int, categoryType: Boolean) {

        when(_formState.value) {
            is ResultState.Success -> {}
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    coroutineScope {
                        val transactionDeferred = async(Dispatchers.IO) {
                            transactionActionRepository.getTransactionById(transactionId)
                        }

                        val categoryDeferred = async(Dispatchers.IO) {
                            categoryRepository.loadCategoriesByType(categoryType)
                        }


                        val transactionResult = transactionDeferred.await()
                        val categoryResult = categoryDeferred.await()


                        if (transactionResult is ResultState.Success && categoryResult is ResultState.Success) {
                            val transactionEdit = transactionResult.data
                            val category = categoryResult.data


                            _formState.value = ResultState.Success(
                                TransactionFormState(
                                    categoryList = category,
                                    selectedCategory = transactionEdit.category,
                                    date = transactionEdit.date,
                                    time = transactionEdit.time,
                                    comment = transactionEdit.comment,
                                    amount = transactionEdit.amount
                            )
                            )
                        } else {

                            val errorMessage = when {
                                transactionResult is ResultState.Error -> transactionResult.message
                                categoryResult is ResultState.Error -> categoryResult.message
                                else -> "Неизвестная ошибка"
                            }

                            // обработка ошибки
                            _formState.value = ResultState.Error(message = errorMessage)
                        }



                    }
                }
            }
        }



    }


    fun handleIntent(intent: TransactionAddIntent) {

        val currentState = _formState.value
        if (currentState !is ResultState.Success) return

        when (intent) {


            is TransactionAddIntent.onAmountChanged -> {

                _formState.update { ResultState.Success(data = currentState.data.copy(amount = intent.amount)) }
            }

            is TransactionAddIntent.onCommentChanged -> {
                _formState.update { ResultState.Success(data = currentState.data.copy(comment = intent.comment)) }
            }

            is TransactionAddIntent.onDateChanged -> {
                _formState.update { ResultState.Success(data = currentState.data.copy(date = intent.date)) }
            }

            is TransactionAddIntent.onTimeChanged -> {
                _formState.update { ResultState.Success(data = currentState.data.copy(time = intent.time)) }
            }

            is TransactionAddIntent.onCategoryChanged -> {
                _formState.update { ResultState.Success(data = currentState.data.copy(selectedCategory = intent.category)) }
            }
        }
    }

    fun editTransaction(transactionId: Int) {
        val currentState = _formState.value

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

                    val result = transactionActionRepository.editTransaction(
                        transactionId = transactionId,
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

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result =  transactionActionRepository.deleteTransactionById(transactionId = transactionId)

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

    fun requestDialogDismiss() {
        _requestState.value = RequestState.Idle
    }

    fun currency(): String {
        return accountRepository.accountCurrency ?: "₽"
    }
}