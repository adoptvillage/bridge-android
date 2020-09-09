package com.adoptvillage.bridge.Service

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient=OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()

        val requestBuilder=original.newBuilder()
            .addHeader("Authorization","")
            .method(original.method,original.body)

        val request=requestBuilder.build()
        chain.proceed(request)
    }.build()

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
    val instance:AuthService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        retrofit.create(AuthService::class.java)
    }


}