package com.example.bankapp.data.remote.model

import com.google.gson.Gson
import retrofit2.Response

data class ErrorResponse(
    val error: String
)


fun parseError(response: Response<*>): String {
    val errorBody = response.errorBody()?.string()
    return try {
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        errorResponse.error
    } catch (e: Exception) {
        errorBody ?: "Unknown error"
    }
}