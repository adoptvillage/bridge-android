package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.DashboardDefaultResponse
import retrofit2.Call
import retrofit2.http.GET

interface DashboardService {
    @GET("user/dashboard")
    fun getUserRole(): Call<DashboardDefaultResponse>
}