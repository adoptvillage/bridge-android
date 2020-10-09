package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardService {
    @GET("user/dashboard")
    fun getUserRole(): Call<DashboardDefaultResponse>

    @POST("user/updatelocation")
    fun updateLocation(
        @Body prefLocationModel: PrefLocationModel
    ): Call<UpdateLocationDefaultResponse>
}