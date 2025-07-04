package com.example.bankapp.data.utils


import com.example.bankapp.core.Constants.Delays
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.model.parseError
import kotlinx.coroutines.delay
import retrofit2.Response


/**
 * Функция получилась больше 20 строк,
 * но мне нравится ее функционал,
 * более лаконичную реализацию не смог придумать
 * */

suspend fun <T, R> safeApiCallList(
    block: suspend () -> Response<List<T>>,
    mapper: (T) -> R
): ResultState<List<R>> {
    var currentRetry = 1


    while (currentRetry <= SafeApiCallConstants.MAX_RETRY) {
        try {
            val response = block()

            if (response.isSuccessful) {
                return ResultState.Success(data = response.body()?.map { mapper(it) }
                    ?: emptyList())

            } else {

                if (response.code() == SafeApiCallConstants.ERROR_CODE_500 && currentRetry < SafeApiCallConstants.MAX_RETRY) {
                    currentRetry++
                    delay(Delays.ERROR_500_RETRY)
                } else {
                    return ResultState.Error(message = parseError(response), code = response.code())
                }

            }

        } catch (e: Exception) {
            return ResultState.Error(
                message = "error: ${e.message}",
                code = null
            )
        }

    }
    return ResultState.Error(message = "unknown error")

}


suspend fun <T, R> safeApiCall(
    block: suspend () -> Response<T>,
    mapper: (T) -> R
): ResultState<R> {
    var currentRetry = 1

    while (currentRetry <= SafeApiCallConstants.MAX_RETRY) {
        try {
            val response = block()

            if (response.isSuccessful) {
                val mappedData = response.body()?.let { mapper(it) }
                    ?: return ResultState.Error(message = "empty response body")
                return ResultState.Success(data = mappedData)
            } else {
                if (response.code() == SafeApiCallConstants.ERROR_CODE_500 && currentRetry < SafeApiCallConstants.MAX_RETRY) {
                    currentRetry++
                    delay(Delays.ERROR_500_RETRY)
                } else {
                    return ResultState.Error(message = parseError(response), code = response.code())
                }
            }
        } catch (e: Exception) {
            return ResultState.Error(
                message = "error: ${e.message}",
                code = null
            )
        }
    }
    return ResultState.Error(message = "unknown error")
}
