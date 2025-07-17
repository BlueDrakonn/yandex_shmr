package com.example.bankapp.data.repository

import com.example.bankapp.core.ResultState
import com.example.bankapp.di.Local
import com.example.bankapp.di.NetworkChecker
import com.example.bankapp.di.Remote
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.CategoryRepository
import com.example.bankapp.domain.repository.WriteRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    @Local private val localCategoryLocalRepositoryImpl: CategoryRepository,
    @Remote private val remoteCategoryRepositoryImpl: CategoryRepository,
    private val writeCategoryRepositoryImpl: WriteRepository<Category>,
    private val networkChecker: NetworkChecker
) : CategoryRepository {
    override suspend fun loadCategories(): ResultState<List<Category>> {
        if (networkChecker.isOnline()) {

            val result = remoteCategoryRepositoryImpl.loadCategories()

            if (result is ResultState.Success) {
                result.data.forEach { category ->
                    writeCategoryRepositoryImpl.addDb(category)
                }
            }
            return result


        } else {

            return localCategoryLocalRepositoryImpl.loadCategories()
        }
    }

    override suspend fun loadCategoriesByType(isIncome: Boolean): ResultState<List<Category>> {
        return if (networkChecker.isOnline()) {
            remoteCategoryRepositoryImpl.loadCategoriesByType(isIncome = isIncome)
        } else {
            localCategoryLocalRepositoryImpl.loadCategoriesByType(isIncome = isIncome)
        }
    }


}