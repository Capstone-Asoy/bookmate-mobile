package com.example.bookmate.utils

import com.example.bookmate.data.response.FailedResponse
import com.google.gson.Gson

fun getErrorMessageFromJson(errorJson: String?): String {
    if (!errorJson.isNullOrBlank()) {
        val gson = Gson()
        val failedResponse = gson.fromJson(errorJson, FailedResponse::class.java)

        return failedResponse.message
    }
    return "Failed request"
}