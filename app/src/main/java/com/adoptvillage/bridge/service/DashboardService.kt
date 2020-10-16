package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.*
import com.adoptvillage.bridge.models.profileModels.GetPrefLoactionDefaultResponse
import com.adoptvillage.bridge.models.profileModels.PrefLocationModel
import com.adoptvillage.bridge.models.profileModels.UpdateLocationDefaultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardService {
    @GET("user/dashboard")
    fun getUserRole(): Call<DashboardDefaultResponse>

    @POST("user/preferredlocation")
    fun updateLocation(
        @Body prefLocationModel: PrefLocationModel
    ): Call<UpdateLocationDefaultResponse>

    @GET("user/preferredlocation")
    fun getPrefLocation(): Call<GetPrefLoactionDefaultResponse>
}