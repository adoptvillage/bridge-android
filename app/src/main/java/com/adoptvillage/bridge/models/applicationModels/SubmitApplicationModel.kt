package com.adoptvillage.bridge.models.applicationModels

import com.google.gson.annotations.SerializedName

data class SubmitApplicationModel(

	@field:SerializedName("area")
	val area: String? = null,

	@field:SerializedName("institute_state")
	val instituteState: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("sub_district")
	val subDistrict: String? = null,

	@field:SerializedName("year_or_semester")
	val yearOrSemester: String? = null,

	@field:SerializedName("course_name")
	val courseName: String? = null,

	@field:SerializedName("offer_letter")
	val offerLetter: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("institution_affiliation_code")
	val institutionAffiliationCode: String? = null,

	@field:SerializedName("applicant_last_name")
	val applicantLastName: String? = null,

	@field:SerializedName("institute_name")
	val instituteName: String? = null,

	@field:SerializedName("contact_number")
	val contactNumber: String? = null,

	@field:SerializedName("applicant_first_name")
	val applicantFirstName: String? = null,

	@field:SerializedName("aadhaar_number")
	val aadhaarNumber: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("fee_structure")
	val feeStructure: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("bank_statement")
	val bankStatement: String? = null,

	@field:SerializedName("institute_district")
	val instituteDistrict: String? = null,

	@field:SerializedName("institute_type")
	val instituteType: Int? = null
)
