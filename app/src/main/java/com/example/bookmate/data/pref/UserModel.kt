package com.example.bookmate.data.pref

data class UserModel(
    val name: String,
    val email: String,
    val token: String,
    val photoUrl: String,
    val isLogin: Boolean = false,
    val isNewUser: Boolean = false,
)