package com.example.bookmate.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BookDetailResponse(

	@field:SerializedName("isBookmarked")
	val isBookmarked: Boolean,

	@field:SerializedName("pageCount")
	val pageCount: Int,

	@field:SerializedName("year")
	val year: Int,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("isbn")
	val isbn: String,

	@field:SerializedName("synopsis")
	val synopsis: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("bookId")
	val bookId: Int,

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem>,

	@field:SerializedName("coverImage")
	val coverImage: String,

	@field:SerializedName("genre")
	val genre: List<String>,

	@field:SerializedName("publisher")
	val publisher: String,

	@field:SerializedName("avgRating")
	val avgRating: String
)

data class ReviewsItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("review")
	val review: String,

	@field:SerializedName("rating")
	val rating: Double,

	@field:SerializedName("userName")
	val userName: String
)

@Parcelize
data class BookParcelize(
	val bookId: Int,
	val title: String,
	val author: String,
	val coverImage: String,
): Parcelable