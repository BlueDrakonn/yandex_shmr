package com.example.bankapp.features.account.accountEdit.models

sealed class AccountEditIntent {
    data class OnAccountUpdate(val name: String, val balance: String, val currency: String) :
        AccountEditIntent()
}