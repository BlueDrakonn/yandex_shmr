package com.example.bankapp.data.repository


import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import com.example.bankapp.data.model.TransactionResponseDto
import com.example.bankapp.data.model.toAccount
import com.example.bankapp.data.model.toTransaction
import com.example.bankapp.data.model.toTransactionDetailed
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.domain.model.Account
import com.example.bankapp.domain.model.Category
import com.example.bankapp.domain.model.Transaction
import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.repository.MainRepository
import com.example.bankapp.domain.viewmodel.ResultState
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//class MainRepositoryImpl(private val apiService: ApiService): MainRepository {
//
//    override suspend fun getTransactions(
//        accountId: Int,
//        startDate: String,
//        endDate: String
//    ): ResultState<List<TransactionResponseDto>> {
//
//        val response = apiService.getTransactions(
//            accountId = accountId,
//            startDate = startDate,
//            endDate = endDate
//        )
//
//
//        return try {
//            val response = apiService.getTransactions(
//                accountId = accountId,
//                startDate = startDate,
//                endDate = endDate
//            )
//
//            ResultState.Success(response)
//
//        } catch (e: Exception) {
//            return ResultState.Error(e.message)
//        }
//    }
//
//    override suspend fun getCategories(): ResultState<List<Category>> {
//        return try {
//            val response = apiService.getCategories()
//            ResultState.Success(response)
//        } catch (e: Exception) {
//            return ResultState.Error(e.message)
//        }
//    }
//
//    override suspend fun getAccounts(): ResultState<List<Account>> {
//        return try {
//            val response = apiService.getAccounts()
//            val mapped = response.map { it.toAccount() }
//            ResultState.Success(mapped)
//        } catch (e: Exception) {
//            ResultState.Error(e.message)
//        }
//    }
//
//    override suspend fun getTodayTransactions(
//        accountId: Int
//    ): ResultState<List<TransactionResponseDto>> {
//        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
//        return try {
//            val response = apiService.getTransactions(
//                accountId = accountId,
//                startDate = today,
//                endDate = today
//            )
//
//            ResultState.Success(response)
//
//        } catch (e: Exception) {
//            return ResultState.Error(e.message)
//        }
//    }
//
//
//}


class MainRepositoryImpl(private val apiService: ApiService) : MainRepository {

    override suspend fun getTransactions(
        accountId: Int,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionResponseDto>> {
        return safeApiCall {
            val response = apiService.getTransactions(
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )
            response.body() ?: emptyList<TransactionResponseDto>()
        }
    }

    override suspend fun getCategories(): ResultState<List<Category>> {
        return safeApiCall {
            apiService.getCategories().body() ?: emptyList<Category>()
        }
    }

    override suspend fun getAccounts(): ResultState<List<Account>> {
        return safeApiCall {
            apiService.getAccounts().body()?.map { it.toAccount() } ?: emptyList()

        }
    }

    override suspend fun getTodayTransactions(
        accountId: Int
    ): ResultState<List<TransactionResponseDto>> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return safeApiCall {
            val response = apiService.getTransactions(
                accountId = accountId,
                startDate = today,
                endDate = today
            )
            response.body() ?: emptyList<TransactionResponseDto>()
        }
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): ResultState<T> {
        var currentRetry = 0
        val maxRetries = 3
        val delayTime = 2000L
        var lastError: Exception? = null
        while (currentRetry <= maxRetries) {
            try {
                val result = block()
                return ResultState.Success(result)
            } catch (e: Exception) {
                lastError = e
                when (e) {
                    is HttpException -> {
                        if (e.code() != 500 || currentRetry == maxRetries) {
                            return ResultState.Error(
                                message = e.response()?.errorBody()?.string() ?: e.message(),
                                code = e.code()
                            )
                        }
                    }
                    is IOException -> {
                        if (currentRetry == maxRetries) {
                            return ResultState.Error(
                                message = "Network error: ${e.message}",
                                code = null
                            )
                        }
                    }
                    else -> {
                        return ResultState.Error(
                            message = "Unknown error: ${e.message}",
                            code = null
                        )
                    }
                }

                delay(delayTime)
                currentRetry++
            }
        }

        return ResultState.Error(
            message = lastError?.message ?: "Unknown error after $maxRetries retries",
            code = (lastError as? HttpException)?.code()
        )
    }
}