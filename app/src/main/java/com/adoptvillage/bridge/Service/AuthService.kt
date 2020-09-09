package com.adoptvillage.bridge.Service

import com.adoptvillage.bridge.Models.Register
import com.adoptvillage.bridge.Models.RegisterDefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Body register: Register
    ): Call<RegisterDefaultResponse>
}