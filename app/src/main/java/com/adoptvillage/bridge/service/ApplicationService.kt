package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApplicationService {
    @POST("application/submit")
    fun submitApplication(
        @Body application: SubmitApplicationModel
    ): Call<SubmitApplicationDefaultResponse>

    @GET("application/")
    fun getApplications(): Call<MutableList<ApplicationResponse>>
}