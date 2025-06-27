package com.example.bankapp.data.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.repository.TodayTransactionRepository
import com.example.bankapp.data.utils.safeApiCall
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Реализация репозитория для загрузки сегодняшних транзакций.
 *
 * Загружает транзакции за текущую дату по заданному id аккаунта через [ApiService].
 *
 * @property apiService Сервис для выполнения сетевых запросов.
 */
class TodayTransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TodayTransactionRepository {

    /**
     * Загружает список транзакций за сегодня для указанного id аккаунта.
     *
     * Выполняет запрос через [ApiService.getTransactions], используя текущую дату
     * как начальную и конечную для фильтрации транзакций.
     *
     * @param accountId ID аккаунта для загрузки транзакций. Не должен быть null.
     * @return [ResultState] с результатом запроса.
     */
    override suspend fun loadTodayTransaction(accountId: Int?): ResultState<List<Transaction>> {

        return safeApiCall(
            mapper = {
                val transaction = it.toTransaction()
                transaction
            },
            block = {
                apiService.getTransactions(
                    accountId = accountId!!,
                    startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                    endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                )
            }
        )
    }
}
