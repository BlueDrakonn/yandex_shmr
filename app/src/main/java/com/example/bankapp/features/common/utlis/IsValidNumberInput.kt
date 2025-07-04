package com.example.bankapp.features.common.utlis

fun isValidNumberInput(input: String): Boolean {
    if (input.isEmpty()) return false

    // Заменяем запятую на точку для унификации
    val normalized = input.replace(',', '.')

    val regex = Regex("^\\d*(\\.\\d*)?\$") // например: "123", "123.45", ".45"
    if (!regex.matches(normalized)) return false


    // Проверяем лишние точки
    val dotCount = normalized.count { it == '.' }
    if (dotCount > 1) return false

    // Проверяем ведущие нули: "0" или "0.xxx" — ок; но "00", "01" — нет
    if (normalized.startsWith("0") && normalized.length > 1 && normalized[1] != '.') {
        return false
    }

    return true
}