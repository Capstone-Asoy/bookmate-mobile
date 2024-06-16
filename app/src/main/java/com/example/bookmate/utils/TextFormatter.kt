package com.example.bookmate.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}
