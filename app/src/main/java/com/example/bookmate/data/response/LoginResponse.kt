package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("isNewAcc")
	val isNewAcc: Boolean,

	@field:SerializedName("auth")
	val auth: Boolean,

	@field:SerializedName("token")
	val token: String
)
