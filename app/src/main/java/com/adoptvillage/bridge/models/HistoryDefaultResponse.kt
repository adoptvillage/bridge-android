package com.adoptvillage.bridge.models

import com.google.gson.annotations.SerializedName

data class HistoryDefaultResponse(

	@field:SerializedName("role")
	val role: Int? = null,

	@field:SerializedName("history")
	val history: List<HistoryItem?>? = null
)

data class HistoryItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("donor_name")
	val donorName: String? = null,

	@field:SerializedName("donation_date")
	val donationDate: String? = null,

	@field:SerializedName("moderator_name")
	val moderatorName: String? = null,

	@field:SerializedName("recipient_name")
	val recipientName: String? = null
)
