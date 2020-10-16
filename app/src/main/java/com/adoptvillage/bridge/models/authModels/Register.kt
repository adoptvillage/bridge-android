package com.adoptvillage.bridge.models.authModels

data class Register (
    val name:String,
    val role:Int,
    val email:String,
    val password:String,
    val otp:String
)


