package com.example.bankapp.data.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.utils.safeApiCall
import javax.inject.Inject


/**
 * Реализация репозитория категорий.
 *
 * Отвечает за загрузку категорий из сети через [ApiService].
 *
 * @property apiService Сервис для сетевых запросов.
 */
class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CategoryRepository {

    /**
     * Загружает список категорий с сервера.
     *
     * Выполняет запрос через [ApiService.getCategories] и возвращает
     * результат, обернутый в [ResultState].
     *
     * @return [ResultState] с результатом запроса.
     */
    override suspend fun loadCategories(): ResultState<List<Category>> {
        return safeApiCall(
            mapper = { it },
            block = { apiService.getCategories() }
        )
    }
}