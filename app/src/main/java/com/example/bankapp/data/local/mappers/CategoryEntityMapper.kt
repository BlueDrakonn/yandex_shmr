package com.example.bankapp.data.local.mappers

import com.example.bankapp.data.local.entity.CategoryEntity
import com.example.bankapp.domain.model.Category

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        emoji = this.emoji,
        isIncome = this.isIncome
    )
}
