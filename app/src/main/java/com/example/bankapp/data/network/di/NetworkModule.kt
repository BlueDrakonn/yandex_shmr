package com.example.bankapp.data.network.di

//@Module
//object NetworkModule {
//
//
//    @Provides
//    @Singleton
//    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
//
//        if (!isInternetAvailable(MyApplication.context)) {
//            throw IOException("No network connection")
//        }
//
//        val original: Request = chain.request()
//        val requestWithToken = original.newBuilder()
//            .header("Authorization", TOKEN)
//            .build()
//
//        chain.proceed(requestWithToken)
//    }
//
//    @Provides
//    @Singleton
//    fun provideHttpClient(
//        authInterceptor: Interceptor
//    ): OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(authInterceptor)
//            .build()
//
//
//
//    @Provides
//    fun provideRetrofit(
//        okHttpClient: OkHttpClient
//    ): Retrofit = Retrofit.Builder()
//        .client(okHttpClient)
//        .baseUrl("https://shmr-finance.ru/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    @Provides
//    fun provideApiService(retrofit: Retrofit): ApiService =
//        retrofit.create(ApiService::class.java)
//}