package com.example.bookmate.di

import android.content.Context
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.pref.UserPreference
import com.example.bookmate.data.pref.dataStore
import com.example.bookmate.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(token)
        return UserRepository.getInstance(apiService, pref)
    }
}