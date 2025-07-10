package com.example.bankapp.features.transactionAction.edit

import androidx.lifecycle.ViewModel
import com.example.bankapp.domain.repository.AccountRepository
import javax.inject.Inject


class TransactionEditViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {}