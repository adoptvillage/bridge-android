package com.adoptvillage.bridge.models

import com.google.gson.annotations.SerializedName

data class AcceptApplicationModel(

	@field:SerializedName("donating_full_amount")
	val donatingFullAmount: Boolean? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("moderator_email")
	val moderatorEmail: String? = null,

	@field:SerializedName("application_id")
	val applicationId: Int? = null
)
