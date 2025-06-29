package com.example.bankapp.features.categories.store.models

sealed class CategoryIntent {
    data class OnSearchQuery(val text: String) : CategoryIntent()
}