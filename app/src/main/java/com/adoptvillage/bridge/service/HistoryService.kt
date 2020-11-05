package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.HistoryDefaultResponse
import retrofit2.Call
import retrofit2.http.GET

interface HistoryService {
    @GET("user/history")
    fun getUserHistory(): Call<HistoryDefaultResponse>
}