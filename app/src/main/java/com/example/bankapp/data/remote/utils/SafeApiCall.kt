package com.example.bankapp.data.remote.utils


import android.util.Log
import com.example.bankapp.core.Constants.Delays
import com.example.bankapp.core.ResultState
import com.example.bankapp.data.remote.model.parseError
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
            Log.d("RESPONCE", "${response.body()} ${response.code()}")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return ResultState.Success(mapper(body))
                } else if (response.code() == 204) {

                    @Suppress("UNCHECKED_CAST")
                    return ResultState.Success(Unit as R)
                } else {

                    return ResultState.Error("empty response body")
                }

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
