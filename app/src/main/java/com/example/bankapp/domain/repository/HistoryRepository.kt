package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.TransactionDetailed
import com.example.bankapp.domain.viewmodel.ResultState
import retrofit2.http.GET

interface HistoryRepository {

    @GET
    suspend fun getTransactions(
        accountId: Int,
        startDate: String,
        endDate: String
    ): ResultState<List<TransactionDetailed>>
}