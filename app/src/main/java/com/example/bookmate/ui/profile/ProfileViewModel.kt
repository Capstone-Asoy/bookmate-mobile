package com.example.bookmate.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.pref.UserModel
import com.example.bookmate.data.response.ProfileResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _profileData = MutableLiveData<ProfileResponse>()
    val profileData: LiveData<ProfileResponse> = _profileData

    private val _errorMessage = MutableLiveData<String>()

    init {
        getSession().observeForever { userModel ->
            _user.value = userModel
        }
    }

    fun getData() {
        _isLoading.value = true

        val client = repository.getApiService().getProfile()

        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>, response: Response<ProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        val userValue = _user.value
                        if (userValue != null && userValue.name.isBlank()) {
                            updateProfileData(responseBody.name, responseBody.image)
                            val newData = UserModel(
                                responseBody.name,
                                userValue.email,
                                userValue.token,
                                responseBody.image,
                                true,
                                userValue.isNewUser
                            )
                            _profileData.value = responseBody
                            _user.value = newData
                        }
                        _errorMessage.value = ""
                        _isError.value = false
                    } else {
                        _errorMessage.value = "Response null"
                        _isError.value = true
                        Log.e(TAG, "Response null")
                    }
                } else {
                    val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                    _errorMessage.value = errorMsg
                    _isError.value = true
                    Log.e(TAG, errorMsg)
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unavailable service 😔"
                _isError.value = true
                Log.e(TAG, "onFailure 2: ${t.message}")
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    private fun updateProfileData(name: String, photoUrl: String) {
        viewModelScope.launch {
            repository.updateProfileData(name, photoUrl)
        }
    }

    private fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error 😔"
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}