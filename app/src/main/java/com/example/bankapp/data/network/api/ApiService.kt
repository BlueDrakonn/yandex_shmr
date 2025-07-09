package com.example.bankapp.data.network.api

import com.example.bankapp.TOKEN
import com.example.bankapp.data.model.AccountDto
import com.example.bankapp.data.model.TransactionResponseDto
import com.example.bankapp.data.model.UpdateAccountRequest
import com.example.bankapp.data.model.UpdateTransactionRequest
import com.example.bankapp.domain.model.Category
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("api/v1/accounts")
    suspend fun getAccounts(
        //@Header("Authorization") auth: String = TOKEN
    ): Response<List<AccountDto>>

    @PUT("api/v1/accounts/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Body request: UpdateAccountRequest
    ): Response<AccountDto>

    @GET("api/v1/categories")
    suspend fun getCategories(
    ): Response<List<Category>>

    @GET("api/v1/categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean,
    ): Response<List<Category>>

    @GET("api/v1/transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
    ): Response<List<TransactionResponseDto>>

    @PUT("api/v1/transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body request: UpdateTransactionRequest
    ): Response<TransactionResponseDto>

    @POST("api/v1/transactions")
    suspend fun addTransaction(
        @Body request: UpdateTransactionRequest
    ): Response<TransactionResponseDto>



}