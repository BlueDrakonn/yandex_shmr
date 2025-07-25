package com.example.bankapp.data.remote.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.api.ApiService
import com.example.bankapp.data.remote.model.UpdateAccountRequest
import com.example.bankapp.data.remote.utils.safeApiCall
import com.example.bankapp.data.remote.utils.safeApiCallList
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.AccountRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


/**
 * Реализация репозитория аккаунтов.
 *
 * Получает данные о счете пользователя через [ApiService].
 * Хранит id аккаунта в [accountId].
 *
 * @property apiService Сервис для сетевых запросов.
 */
class RemoteAccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AccountRepository {


    /**
     * ID текущего  аккаунта.
     */
    override var accountId: Int? = null

    /**
     * Currency текущего  аккаунта.
     */
    override var accountCurrency: String? = null

    /**
     * Error возникший при получение аккаунта, прокидывается на другие скрины.
     */
    override var accountError: String? = null

    /**
     * Загружает список аккаунтов пользователя с сервера(по умолчанию в списке один аккаунт).
     *
     * Выполняет запрос через [ApiService.getAccounts], преобразует
     * данные и сохраняет ID первого аккаунта, если он есть.
     *
     * @return [ResultState] с результатом запроса.
     */
    override suspend fun loadAccounts(): ResultState<List<Account>> {
        val result = safeApiCallList(
            mapper = { it.toAccount() },
            block = { apiService.getAccounts() }
        )
        when (result) {
            is ResultState.Success -> {
                accountId = result.data.firstOrNull()?.userId
                accountCurrency = result.data.firstOrNull()?.currency
                return result
            }

            is ResultState.Error -> {
                accountError = result.message
                return result
            }

            else -> return result
        }
    }

    override suspend fun updateAccount(
        request: UpdateAccountRequest
    ): ResultState<Account> {
        return if (accountId != null) {
            safeApiCall(
                mapper = { it.toAccount() },
                block = {
                    apiService.updateAccount(
                        accountId = accountId!!,
                        request = request
                    )
                })
        } else {
            ResultState.Error(message = accountError)
        }
    }

    override suspend fun loadTransactionsForChart(): ResultState<List<TransactionDetailed>> {
        return safeApiCallList(
            mapper = {
                it.toTransactionDetailed()
            },
            block = {
                apiService.getTransactions(
                    accountId ?: 28,
                    startDate = LocalDate.of(LocalDate.now().year, 1, 1)
                        .format(DateTimeFormatter.ISO_DATE),
                    endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                )
            }
        )
    }

}