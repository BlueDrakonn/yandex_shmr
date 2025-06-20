package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.viewmodel.ResultState

import kotlinx.coroutines.flow.StateFlow

interface CategoryRepository {
    val categoryState: StateFlow<ResultState<List<Category>>>
    suspend fun loadCategories()
}