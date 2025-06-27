package com.example.bankapp.data.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.utils.safeApiCall
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(
    private  val apiService: ApiService
):CategoryRepository {

    override suspend fun loadCategories(): ResultState<List<Category>> {

        return safeApiCall(
            mapper = { it },
            block = { apiService.getCategories()  }
        )
    }
}