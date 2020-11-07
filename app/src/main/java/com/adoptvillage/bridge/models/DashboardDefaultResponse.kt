package com.adoptvillage.bridge.models

import com.google.gson.annotations.SerializedName

data class DashboardDefaultResponse(

	@field:SerializedName("role")
	val role: Int? = null,

	@field:SerializedName("applications")
	val applications: List<ApplicationsItem?>? = null
)

data class ApplicationsItem(

	@field:SerializedName("area")
	val area: String? = null,

	@field:SerializedName("institute_state")
	val instituteState: String? = null,

	@field:SerializedName("sub_district")
	val subDistrict: String? = null,

	@field:SerializedName("donor_name")
	val donorName: String? = null,

	@field:SerializedName("offer_letter")
	val offerLetter: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("donating_amount")
	val donatingAmount: Int? = null,

	@field:SerializedName("applicant_last_name")
	val applicantLastName: String? = null,

	@field:SerializedName("applicant_first_name")
	val applicantFirstName: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("moderator_id")
	val moderatorId: String? = null,

	@field:SerializedName("moderator_name")
	val moderatorName: String? = null,

	@field:SerializedName("reserved_application_id")
	val reservedApplicationId: Int? = null,

	@field:SerializedName("fee_structure")
	val feeStructure: String? = null,

	@field:SerializedName("institute")
	val institute: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("donor_id")
	val donorId: String? = null,

	@field:SerializedName("bank_statement")
	val bankStatement: String? = null,

	@field:SerializedName("institute_district")
	val instituteDistrict: String? = null,

	@field:SerializedName("no_of_donors")
	val noOfDonors: Int? = null,

	@field:SerializedName("recipient_id")
	val recipientId: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
