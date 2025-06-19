package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.viewmodel.ResultState

interface CategoryRepository {
    suspend fun getCategories(): ResultState<List<Category>>
}