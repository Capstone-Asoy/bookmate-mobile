package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class AddBookmarkResponse(

	@field:SerializedName("bookmarkId")
	val bookmarkId: String,

	@field:SerializedName("message")
	val message: String
)
