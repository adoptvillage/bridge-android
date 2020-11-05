package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.applicationModels.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    @GET("applications/filter")
    fun getFilteredApplications(
        @Query("area")area:String,
        @Query("sub_district")subDistrict:String,
        @Query("district")district:String,
        @Query("state")state:String
    ):Call<MutableList<ApplicationResponse>>
}