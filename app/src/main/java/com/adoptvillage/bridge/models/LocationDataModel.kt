package com.adoptvillage.bridge.models

import com.google.gson.annotations.SerializedName

data class LocationDataModel(
	val states: List<StatesItem?>? = null
)

data class SubDistrictsItem(
	val villages: List<String?>? = null,
	@field:SerializedName("sub-district")
	val subDistrict: String? = null
)

data class DistrictsItem(
	val district: String? = null,
	@field:SerializedName("sub-districts")
	val subDistricts: List<SubDistrictsItem?>? = null
)

data class StatesItem(
	val districts: List<DistrictsItem?>? = null,
	val state: String? = null
)

