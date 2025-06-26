package com.example.bankapp.data.network.api

import com.example.bankapp.TOKEN
import com.example.bankapp.data.model.AccountDto
import com.example.bankapp.data.model.TransactionResponseDto
import com.example.bankapp.domain.model.Category
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("api/v1/accounts")
    suspend fun getAccounts(
        //@Header("Authorization") auth: String = TOKEN
    ): Response<List<AccountDto>>

    @GET("api/v1/categories")
    suspend fun getCategories(
        //@Header("Authorization") auth: String = TOKEN
    ): Response<List<Category>>

    @GET("api/v1/transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        //@Header("Authorization") token: String = TOKEN
    ): Response<List<TransactionResponseDto>>
}