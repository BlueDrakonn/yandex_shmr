package com.example.bankapp.features.account.store.models

sealed class AccountIntent {
    data class OnAccountUpdate(val name: String, val balance: String, val currency: String) :
        AccountIntent()
}