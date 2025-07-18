package com.example.bankapp.data.remote.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.api.ApiService
import com.example.bankapp.data.remote.utils.safeApiCallList
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import javax.inject.Inject


/**
 * Реализация репозитория категорий.
 *
 * Отвечает за загрузку категорий из сети через [ApiService].
 *
 * @property apiService Сервис для сетевых запросов.
 */
class RemoteCategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CategoryRepository {


    override suspend fun loadCategories(): ResultState<List<Category>> {

        return safeApiCallList(
            mapper = { it },
            block = { apiService.getCategories() }
        )
    }


    override suspend fun loadCategoriesByType(isIncome: Boolean): ResultState<List<Category>> {

        return safeApiCallList(
            mapper = { it },
            block = { apiService.getCategoriesByType(isIncome = isIncome) }
        )
    }


}