package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class GenreResponse(
	@field:SerializedName("genres")
	val genres: List<String>
)
