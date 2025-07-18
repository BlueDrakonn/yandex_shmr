package com.example.bankapp.di.remote

import android.content.Context
import com.example.bankapp.TOKEN
import com.example.bankapp.data.remote.api.ApiService
import com.example.bankapp.data.remote.utils.isInternetAvailable
import com.example.bankapp.di.DefaultNetworkChecker
import com.example.bankapp.di.NetworkChecker
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
object NetworkModule {


    @Provides
    @Singleton
    fun provideAuthInterceptor(context: Context): Interceptor = Interceptor { chain ->

        if (!isInternetAvailable(context)) {
            throw IOException("No network connection")
        }

        val original: Request = chain.request()
        val requestWithToken = original.newBuilder()
            .header("Authorization", TOKEN)
            .build()

        chain.proceed(requestWithToken)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        authInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()



    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {

        val gson = GsonBuilder().serializeNulls().create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://shmr-finance.ru/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideNetworkChecker(defaultNetworkChecker: DefaultNetworkChecker): NetworkChecker = defaultNetworkChecker

}