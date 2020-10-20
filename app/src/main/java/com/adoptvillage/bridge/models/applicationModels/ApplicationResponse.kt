package com.adoptvillage.bridge.models.applicationModels

import com.google.gson.annotations.SerializedName

data class ApplicationResponse(

	@field:SerializedName("area")
	val area: String? = null,

	@field:SerializedName("institute_state")
	val instituteState: String? = null,

	@field:SerializedName("sub_district")
	val subDistrict: String? = null,

	@field:SerializedName("remaining_amount")
	val remainingAmount: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("applicant_last_name")
	val applicantLastName: String? = null,

	@field:SerializedName("applicant_first_name")
	val applicantFirstName: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("institute")
	val institute: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("institute_district")
	val instituteDistrict: String? = null,

	@field:SerializedName("no_of_donors")
	val noOfDonors: Int? = null
)
