package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileService {
    @GET("user/profile")
    fun getUserProfile(): Call<ProfileDefaultResponse>

    @PUT("user/profile")
    fun updateProfile(
        @Body updateProfileModel: UpdateProfileModel
    ): Call<UpdateProfileDefaultResponse>
}
