package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("list_image")
	val listImage: List<String>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("list_rating")
	val listRating: Int,

	@field:SerializedName("haveHistory")
	val haveHistory: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("statusCode")
	val statusCode: String,

	@field:SerializedName("reading_list")
	val readingList: Int,

	@field:SerializedName("list_judul")
	val listJudul: List<String>
)
