package com.adoptvillage.bridge.models.profileModels

import com.google.gson.annotations.SerializedName

data class PrefLocationModel(

	@field:SerializedName("area")
	val area: String? = "",

	@field:SerializedName("sub_district")
	val subDistrict: String? = "",

	@field:SerializedName("district")
	val district: String? = "",

	@field:SerializedName("state")
	val state: String? = ""
)
