package com.adoptvillage.bridge.models

import com.google.gson.annotations.SerializedName

data class LoginDefaultResponse(

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("expiresIn")
    val expiresIn: String? = null,

    @field:SerializedName("kind")
    val kind: String? = null,

    @field:SerializedName("displayName")
    val displayName: String? = null,

    @field:SerializedName("idToken")
    val idToken: String? = null,

    @field:SerializedName("registered")
    val registered: Boolean? = null,

    @field:SerializedName("localId")
    val localId: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null

)

