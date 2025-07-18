package com.example.bankapp.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.local.dao.CategoryDao
import com.example.bankapp.data.local.mappers.toEntity
import com.example.bankapp.domain.mapper.toDomain
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.WriteRepository
import javax.inject.Inject

class LocalCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository, WriteRepository<Category> {


    override suspend fun loadCategories(): ResultState<List<Category>> {


        try {
            val result = categoryDao.getAllCategories().map { it.toDomain() }

            return ResultState.Success(data = result)

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }


    override suspend fun loadCategoriesByType(isIncome: Boolean): ResultState<List<Category>> {
        try {
            val result = categoryDao.getCategoryByType(isIncome = isIncome).map { it.toDomain() }

            return ResultState.Success(data = result)

        } catch (e: SQLiteConstraintException) {
            return ResultState.Error(message = e.message)
        }
    }

    override suspend fun addDb(entity: Category) {
        categoryDao.insertAccount(entity.toEntity())
    }

}