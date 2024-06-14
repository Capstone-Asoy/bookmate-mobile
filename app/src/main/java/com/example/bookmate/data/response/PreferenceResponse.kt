package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class PreferenceResponse(
    @field:SerializedName("statusCode")
    val statusCode: String,

    @field:SerializedName("message")
    val message: String
)