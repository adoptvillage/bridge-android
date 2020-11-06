package com.adoptvillage.bridge.models.chatModels

import com.google.gson.annotations.SerializedName

data class ChatDefaultResponse(

	@field:SerializedName("message")
	val message: String = ""
)
