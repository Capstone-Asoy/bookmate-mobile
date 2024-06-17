package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class FilterGenreResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("fields")
	val fields: List<BookItem>,

	@field:SerializedName("statusCode")
	val statusCode: String
)