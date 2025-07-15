package com.example.bankapp.features.analysis.models.utils

import android.annotation.SuppressLint
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.features.analysis.models.AnalysisCategoryUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("DefaultLocale")
fun List<TransactionDetailed>.toUiModel(): List<AnalysisCategoryUi> {
    // Сначала сгруппируем транзакции по категории
    val groupedByCategory: Map<Category, List<TransactionDetailed>> = this.groupBy { it.category }

    // Суммируем amount по каждой категории
    val categoryToAmount: Map<Category, Double> = groupedByCategory.mapValues { (_, txns) ->
        txns.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    }

    // Считаем общую сумму всех транзакций
    val totalAmount: Double = categoryToAmount.values.sum()

    // Преобразуем в список AnalysisCategoryUi с подсчитанным процентом
    return categoryToAmount.map { (category, amount) ->
        val rawPercent = if (totalAmount == 0.0) 0f else ((amount / totalAmount) * 100).toFloat()
        val roundedPercent = String.format("%.1f", rawPercent).replace(",", ".").toFloat()
        AnalysisCategoryUi(
            category = category,
            amount = amount,
            percent = roundedPercent
        )
    }
}


fun String.formatToFullDate(): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("d LLLL yyyy", Locale("ru"))
        val parsedDate = LocalDate.parse(this, inputFormatter)
        parsedDate.format(outputFormatter).replaceFirstChar { it.uppercaseChar() }
    } catch (e: Exception) {
        this // если ошибка парсинга — вернуть как есть
    }
}




