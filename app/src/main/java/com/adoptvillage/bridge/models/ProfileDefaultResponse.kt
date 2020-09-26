package com.adoptvillage.bridge.models

import com.google.gson.annotations.SerializedName

data class ProfileDefaultResponse(

	@field:SerializedName("profile_image")
	val profileImage: Any? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("is_email_verified")
	val isEmailVerified: Boolean? = null,

	@field:SerializedName("firebase_id")
	val firebaseId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("is_donor")
	val isDonor: Boolean? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_moderator")
	val isModerator: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("is_recipient")
	val isRecipient: Boolean? = null
)
