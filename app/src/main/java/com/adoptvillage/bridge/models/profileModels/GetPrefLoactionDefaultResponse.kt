package com.adoptvillage.bridge.models.profileModels

import com.google.gson.annotations.SerializedName

data class GetPrefLocationDefaultResponse(

	@field:SerializedName("area")
	val area: String? = "Village",

	@field:SerializedName("sub_district")
	val subDistrict: String? = "Sub-District",

	@field:SerializedName("district")
	val district: String? = "District",

	@field:SerializedName("state")
	val state: String? = "State"
)
