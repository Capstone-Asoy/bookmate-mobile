package com.example.bookmate.data.response

data class ProfileResponse(
    val image: String,
    val listImage: List<String>,
    val name: String,
    val listRating: Int,
    val haveHistory: Boolean,
    val message: String,
    val email: String,
    val statusCode: String,
    val readingList: Int,
    val listJudul: List<String>
)

