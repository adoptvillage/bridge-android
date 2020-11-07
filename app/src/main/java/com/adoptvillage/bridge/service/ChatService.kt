package com.adoptvillage.bridge.service

import com.adoptvillage.bridge.models.chatModels.ChatDefaultResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatService {

    @POST("application/donate")
    fun donate(
        @Query(value = "reserved_application_id")reserved_application_id:String
    ): Call<ChatDefaultResponse>

    @POST("application/verify")
    fun verify(
        @Query(value = "reserved_application_id")reserved_application_id:String
    ): Call<ChatDefaultResponse>

    @POST("application/close")
    fun close(
        @Query(value = "reserved_application_id")reserved_application_id:String
    ): Call<ChatDefaultResponse>
}
