package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.applicationModels.FilterApplicationModel
import com.adoptvillage.bridge.models.applicationModels.*
import retrofit2.Call
import retrofit2.http.*

interface ApplicationService {
    @POST("application/submit")
    fun submitApplication(
        @Body application: SubmitApplicationModel
    ): Call<SubmitApplicationDefaultResponse>

    @GET("application/")
    fun getApplications(): Call<MutableList<ApplicationResponse>>

    @POST("application/accept")
    fun acceptApplication(
        @Body application: AcceptApplicationModel
    ): Call<AcceptApplicationDefaultResponse>
    
    @POST("application/filter")
    fun getFilteredApplications(
        @Body filter: FilterApplicationModel
    ):Call<MutableList<ApplicationResponse>>
}