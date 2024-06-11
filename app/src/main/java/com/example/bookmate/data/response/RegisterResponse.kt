package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("auth")
	val auth: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: String,

	@field:SerializedName("token")
	val token: String
)

data class Data(

	@field:SerializedName("isSucces")
	val isSucces: Int,

	@field:SerializedName("id")
	val id: String
)
