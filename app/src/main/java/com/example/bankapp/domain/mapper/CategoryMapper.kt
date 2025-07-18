package com.example.bankapp.domain.mapper

import com.example.bankapp.data.local.entity.CategoryEntity
import com.example.bankapp.domain.model.Category

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
}