package com.example.bookmate.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.pref.UserModel
import com.example.bookmate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        updateApiService()
        return repository.getSession().asLiveData()
    }

    private fun updateApiService() {
        viewModelScope.launch {
            val token = repository.getToken()
            val apiService = ApiConfig.getApiService(token)
            repository.updateApiService(apiService)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}