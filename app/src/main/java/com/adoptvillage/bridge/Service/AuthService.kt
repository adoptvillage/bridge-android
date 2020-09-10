package com.adoptvillage.bridge.Service

import com.adoptvillage.bridge.Models.Register
import com.adoptvillage.bridge.Models.RegisterDefaultResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @POST("user/register")
    fun registerUser(
        @Body register: Register
    ): Call<RegisterDefaultResponse>
}