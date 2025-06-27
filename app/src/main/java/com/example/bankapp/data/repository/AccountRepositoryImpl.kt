package com.example.bankapp.data.repository


import com.example.bankapp.core.ResultState
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.repository.AccountRepository
import com.example.bankapp.utils.safeApiCall
import javax.inject.Inject


/**
 * Реализация репозитория аккаунтов.
 *
 * Получает данные о счете пользователя через [ApiService].
 * Хранит id аккаунта в [accountId].
 *
 * @property apiService Сервис для сетевых запросов.
 */
class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AccountRepository {

    /**
     * ID текущего  аккаунта.
     */
    override var accountId: Int? = null

    /**
     * Загружает список аккаунтов пользователя с сервера(по умолчанию в списке один аккаунт).
     *
     * Выполняет запрос через [ApiService.getAccounts], преобразует
     * данные и сохраняет ID первого аккаунта, если он есть.
     *
     * @return [ResultState] с результатом запроса.
     */
    override suspend fun loadAccounts(): ResultState<List<Account>> {
        val result = safeApiCall(
            mapper = { it.toAccount() },
            block = { apiService.getAccounts() }
        )
        when (result) {
            is ResultState.Success -> {
                accountId = result.data.firstOrNull()?.userId
                return result
            }
            else -> return result
        }
    }
}