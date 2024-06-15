package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val data: List<BookItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: String
)

data class BookItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("books_id")
	val booksId: Int,

	@field:SerializedName("judul")
	val judul: String
)
