package com.example.bankapp.features.transactionAction.edit

import androidx.lifecycle.ViewModel
import com.example.bankapp.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionEditViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {}