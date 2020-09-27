package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.ProfileDefaultResponse
import com.adoptvillage.bridge.models.RegisterDefaultResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProfileService {
    @GET("user/profile")
    fun getUserProfile(): Call<ProfileDefaultResponse>

}
