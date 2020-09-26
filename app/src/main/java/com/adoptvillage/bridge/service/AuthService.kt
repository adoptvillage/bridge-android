package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.Login
import com.adoptvillage.bridge.models.LoginDefaultResponse
import com.adoptvillage.bridge.models.Register
import com.adoptvillage.bridge.models.RegisterDefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("user/register")
    fun registerUser(
        @Body register: Register
    ): Call<RegisterDefaultResponse>


    @POST("user/login")
    fun loginUser(
        @Body login: Login
    ): Call<LoginDefaultResponse>
}

