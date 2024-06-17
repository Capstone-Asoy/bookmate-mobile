package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class AddReviewResponse(

	@field:SerializedName("books_id")
	val booksId: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: String
)
