package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.authModels.Register
import com.adoptvillage.bridge.models.authModels.RegisterDefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("user/register")
    fun registerUser(
        @Body register: Register
    ): Call<RegisterDefaultResponse>

}

