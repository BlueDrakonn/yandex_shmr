package com.example.bankapp.mock.repository

import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.repository.Repository
import com.example.bankapp.data.model.AccountBrief
import com.example.bankapp.data.model.TransactionResponse
import kotlinx.coroutines.delay
import java.time.LocalDateTime


class MockRepository : Repository {

    val mockTransactions = listOf(
        TransactionResponse(
            id = 1,
            account = AccountBrief(
                id = 1,
                name = "Основной счёт",
                balance = 1000.00,
                currency = "₽"
            ),
            category = Category(
                id = 1,
                title = "Зарплата",
                icon = "💰",
                isIncome = true
            ),
            amount = 500.00,
            transactionDate = LocalDateTime.parse("2025-06-12T09:00:00"),
            comment = "Зарплата за июнь",
            createdAt = LocalDateTime.parse("2025-06-12T09:01:00"),
            updatedAt = LocalDateTime.parse("2025-06-12T09:01:00")
        ),
        TransactionResponse(
            id = 2,
            account = AccountBrief(
                id = 1,
                name = "Основной счёт",
                balance = 950.00,
                currency = "₽"
            ),
            category = Category(
                id = 2,
                title = "Продукты",
                icon = "🛒",
                isIncome = false
            ),
            amount = 50.00,
            transactionDate = LocalDateTime.parse("2025-06-12T12:30:00"),
            comment = "Покупка в магазине",
            createdAt = LocalDateTime.parse("2025-06-12T12:31:00"),
            updatedAt = LocalDateTime.parse("2025-06-12T12:31:00")
        ),
        TransactionResponse(
            id = 3,
            account = AccountBrief(
                id = 1,
                name = "Основной счет",
                balance = 5000.00,
                currency = "₽"
            ),
            category = Category(
                id = 3,
                title = "Развлечения",
                icon = "🎮",
                isIncome = false
            ),
            amount = 300.00,
            transactionDate = LocalDateTime.parse("2025-06-11T20:15:00"),
            comment = "Подписка на Netflix",
            createdAt = LocalDateTime.parse("2025-06-11T20:16:00"),
            updatedAt = LocalDateTime.parse("2025-06-11T20:16:00")
        ),
        TransactionResponse(
            id = 4,
            account = AccountBrief(
                id = 1,
                name = "Основной счет",
                balance = 4800.00,
                currency = "₽"
            ),
            category = Category(
                id = 4,
                title = "Кафе",
                icon = "☕",
                isIncome = false
            ),
            amount = 200.00,
            transactionDate = LocalDateTime.parse("2025-06-12T10:00:00"),
            comment = "Завтрак в кафе",
            createdAt = LocalDateTime.parse("2025-06-12T10:01:00"),
            updatedAt = LocalDateTime.parse("2025-06-12T10:01:00")
        ),

        TransactionResponse(
            id = 5,
            account = AccountBrief(
                id = 1,
                name = "Основной счет",
                balance = 4600.00,
                currency = "₽"
            ),
            category = Category(
                id = 5,
                title = "Фриланс",
                icon = "🧑‍💻",
                isIncome = true
            ),
            amount = 1000.00,
            transactionDate = LocalDateTime.parse("2025-06-12T15:30:00"),
            comment = "Оплата за проект",
            createdAt = LocalDateTime.parse("2025-06-12T15:31:00"),
            updatedAt = LocalDateTime.parse("2025-06-12T15:31:00")
        ),

        TransactionResponse(
            id = 6,
            account = AccountBrief(
                id = 1,
                name = "Основной счет",
                balance = 4550.00,
                currency = "₽"
            ),
            category = Category(
                id = 6,
                title = "Транспорт",
                icon = "🚗",
                isIncome = false
            ),
            amount = 50.00,
            transactionDate = LocalDateTime.parse("2025-06-12T18:00:00"),
            comment = null,
            createdAt = LocalDateTime.parse("2025-06-12T18:01:00"),
            updatedAt = LocalDateTime.parse("2025-06-12T18:01:00")
        )

    )



    override suspend fun getAccounts(): List<Account> {
        delay(5000)


        val shouldThrowError = (0..3).random() == 0
        if (shouldThrowError) {
            throw RuntimeException("error for testing states")
        } else {
            return listOf(
                Account(
                    id = 1,
                    userId = 1,
                    name = "Основной счёт",
                    balance = 100000.0,
                    currency = "₽",
                    createdAt = LocalDateTime.now().minusDays(10),
                    updatedAt = LocalDateTime.now()
                )
            )
        }
    }


    override suspend fun getCategories(): List<Category> {
        return listOf(

            Category(id = 1, title = "Зарплата", icon = "💰", isIncome = true),
            Category(id = 2, title = "Фриланс", icon = "🧑‍💻", isIncome = true),
            Category(id = 3, title = "Подарок", icon = "🎁", isIncome = true),
            Category(id = 4, title = "Продукты", icon = "🍎", isIncome = false),
            Category(id = 5, title = "Кафе", icon = "☕", isIncome = false),
            Category(id = 6, title = "Транспорт", icon = "🚗", isIncome = false),
            Category(id = 7, title = "Одежда", icon = "👕", isIncome = false),
            Category(id = 8, title = "Развлечения", icon = "🎮", isIncome = false),
            Category(id = 9, title = "Медицина", icon = "💊", isIncome = false),
            Category(id = 10, title = "Аренда", icon = "🏠", isIncome = false),
            Category(id = 11, title = "Ремонт квартиры", icon = "РК", isIncome = false)
        )
    }

    override suspend fun getTodayTransactionsByAccount(
        accountId: Int,
    ): List<TransactionResponse> {

        return mockTransactions.filter {
            it.account.id == accountId
        }
    }
}
