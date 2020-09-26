package com.adoptvillage.bridge.service

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    val authService: AuthService
    val profileService : ProfileService
    lateinit var idToken:String

    companion object {
        private var retrofitClient: RetrofitClient? = null

        val instance: RetrofitClient
            get() {
                if (retrofitClient == null) {
                    retrofitClient = RetrofitClient()
                }
                return retrofitClient as RetrofitClient
            }
    }

    init {

        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", idToken)
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://bridge-temp.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retrofitWithHeader = Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://bridge-temp.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        authService=retrofit.create(AuthService::class.java)
        profileService=retrofitWithHeader.create(ProfileService::class.java)

    }
}

