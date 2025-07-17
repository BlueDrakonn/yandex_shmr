package com.example.bankapp.data.remote.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.api.ApiService
import com.example.bankapp.data.remote.utils.safeApiCallList
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.HistoryTransactionRepository
import javax.inject.Inject

/**
 * Реализация репозитория истории транзакций.
 *
 * Загружает транзакции за выбранный период по заданному id аккаунта через [ApiService].
 *
 * @property apiService Сервис для выполнения сетевых запросов.
 */
class HistoryTransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HistoryTransactionRepository {

    /**
     * Загружает историю транзакций для указанного id за заданный период.
     *
     * Выполняет запрос через [ApiService.getTransactions], преобразует полученные
     * данные в список [TransactionDetailed] и возвращает результат в [ResultState].
     *
     * @param accountId ID аккаунта для загрузки истории.
     * @param startDate Начальная дата периода в формате строки.
     * @param endDate Конечная дата периода в формате строки.
     * @return [ResultState] с результатом запроса.
     */
    override suspend fun loadHistoryTransaction(
        accountId: Int?,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionDetailed>> {



        return safeApiCallList(
            mapper = {
                it.toTransactionDetailed()
            },
            block = {apiService.getTransactions(
                accountId = accountId!!,
                startDate = startDate,
                endDate = endDate
            )
            }
        )
    }
}


