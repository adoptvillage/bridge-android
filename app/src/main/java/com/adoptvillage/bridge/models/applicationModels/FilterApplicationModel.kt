package com.adoptvillage.bridge.models.applicationModels

import com.google.gson.annotations.SerializedName

data class FilterApplicationModel(

	@field:SerializedName("area")
	val area: String? = null,

	@field:SerializedName("sub_district")
	val subDistrict: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("state")
	val state: String? = null
)
