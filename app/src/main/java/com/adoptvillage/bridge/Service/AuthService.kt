package com.adoptvillage.bridge.Service

import com.adoptvillage.bridge.Models.Login
import com.adoptvillage.bridge.Models.LoginDefaultResponse
import com.adoptvillage.bridge.Models.Register
import com.adoptvillage.bridge.Models.RegisterDefaultResponse
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

