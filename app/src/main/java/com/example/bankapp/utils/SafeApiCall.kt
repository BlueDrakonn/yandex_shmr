package com.example.bankapp.utils

import android.util.Log
import android.widget.Toast
import com.example.bankapp.MyApplication
import com.example.bankapp.R
import com.example.bankapp.data.model.parseError
import com.example.bankapp.domain.viewmodel.ResultState
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T, R> safeApiCall(block: suspend () -> Response<List<T>>, mapper: (T) -> R): ResultState<List<R>> {
    var currentRetry = 1
    val maxRetries = 3

    var lastErrorBody: String? = null
    val context = MyApplication.context

    if (!isInternetAvailable(context)) {
        return ResultState.Error(message = "no internet connection")
    }

    while (currentRetry <= maxRetries) {

        try {
            val response = block()

            if (response.isSuccessful) {

                val body = response.body() ?: emptyList()
                val mapped = body.map { mapper(it) }
                return ResultState.Success(data = mapped)

            } else {
                val errorCode = response.code()
                val errorMessage = parseError(response)
                lastErrorBody = errorMessage

                if (errorCode == 500 && currentRetry < maxRetries) {

                    currentRetry++
                    delay(Delays.ERROR_500_RETRY)
                } else {
                    return ResultState.Error(message = errorMessage, code = errorCode)
                }

            }

        } catch (e: Exception) {
            return ResultState.Error(
                message = "error: ${e.message}",
                code = null
            )
        }



    }

    return ResultState.Error(
        message = lastErrorBody ?: "Unknown error after $maxRetries retries",
        code = null
    )

}