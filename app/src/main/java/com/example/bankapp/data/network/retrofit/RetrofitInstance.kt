package com.example.bankapp.data.network.retrofit

import com.example.bankapp.MyApplication
import com.example.bankapp.data.network.api.ApiService
import com.example.bankapp.data.network.interceptor.ConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://shmr-finance.ru/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ConnectionInterceptor(context = MyApplication.context))
        .build()


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)

            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

