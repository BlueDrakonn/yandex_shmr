package com.example.bankapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.bankapp.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE isIncome = :isIncome")
    suspend fun getCategoryByType(isIncome: Boolean): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getCategoryById(id: Int): CategoryEntity

}
