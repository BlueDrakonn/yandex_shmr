package com.example.bankapp.data.network.interceptor

import android.content.Context
import com.example.bankapp.TOKEN
import com.example.bankapp.utils.isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response

class ConnectionInterceptor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {


//        if(!isInternetAvailable(context=context)) {
//
//        } думаю как в ui сообщение прокидывать

        val request = chain.request().newBuilder()
            .addHeader("Authorization", TOKEN)
            .build()

        return chain.proceed(request)


    }
}
