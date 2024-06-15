package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("results")
	val results: List<BookItem>
)
