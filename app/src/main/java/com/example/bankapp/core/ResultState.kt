package com.example.bankapp.core


sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(
        val message: String?,
        val code: Int? = null,
        val throwable: Throwable? = null
    ) : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()
}