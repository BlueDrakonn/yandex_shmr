package com.example.bankapp.data.remote.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.api.ApiService
import com.example.bankapp.data.remote.utils.safeApiCallList
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.TransactionRepository
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
class RemoteTransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TransactionRepository {

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


        return safeApiCallList(
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
