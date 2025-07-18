package com.example.bankapp.domain.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.domain.model.Category

interface CategoryRepository {

    suspend fun loadCategories(): ResultState<List<Category>>


    suspend fun loadCategoriesByType(isIncome: Boolean): ResultState<List<Category>>
}