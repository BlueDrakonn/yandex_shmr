package com.example.bankapp.features.firstLaunch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.FirstLaunchManager
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val firstLaunchManager: FirstLaunchManager
) : ViewModel() {

    fun start(){
        if (firstLaunchManager.isFirstLaunch()) {

            viewModelScope.launch(Dispatchers.IO) {
                if (categoryRepository.loadCategories() !is ResultState.Success) return@launch

                if (accountRepository.loadAccounts() !is ResultState.Success) return@launch

                if (transactionRepository.loadHistoryTransaction(
                        accountId = accountRepository.accountId,
                        startDate = LocalDate.now()
                            .withDayOfYear(1)
                            .format(DateTimeFormatter.ISO_DATE),
                        endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                    ) !is ResultState.Success
                ) return@launch

            }

            firstLaunchManager.markAppLaunched()
        }

    }

}