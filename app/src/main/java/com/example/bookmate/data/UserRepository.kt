package com.example.bookmate.data

import com.example.bookmate.data.pref.UserModel
import com.example.bookmate.data.pref.UserPreference
import com.example.bookmate.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private var apiService: ApiService,
    private val userPreference: UserPreference,
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getToken(): String {
        return userPreference.getToken().first()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getApiService(): ApiService {
        return apiService
    }

    fun updateApiService(newApiService: ApiService) {
        apiService = newApiService
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService, userPreference: UserPreference
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userPreference)
        }.also { instance = it }
    }
}