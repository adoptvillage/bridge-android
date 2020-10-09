package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.Register
import com.adoptvillage.bridge.models.RegisterDefaultResponse
import com.adoptvillage.bridge.models.SubmitApplicationDefaultResponse
import com.adoptvillage.bridge.models.SubmitApplicationModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SubmitApplicationService {
    @POST("application/submit")
    fun submitApplication(
        @Body application: SubmitApplicationModel
    ): Call<SubmitApplicationDefaultResponse>
}