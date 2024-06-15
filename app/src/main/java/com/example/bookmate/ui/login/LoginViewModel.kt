package com.example.bookmate.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.pref.UserModel
import com.example.bookmate.data.request.LoginRequest
import com.example.bookmate.data.response.LoginResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun login(email: String, password: String) {
        _isLoading.value = true

        if (validateInput(email, password)) {
            val client = repository.getApiService().login(LoginRequest(email, password))

            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>, response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val user = UserModel(
                                "",
                                email,
                                responseBody.token,
                                "",
                                isLogin = true,
                                isNewUser = responseBody.isNewAcc
                            )
                            saveSession(user)

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

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Unavailable service 😔"
                    _isError.value = true
                    Log.e(TAG, "onFailure 2: ${t.message}")
                }
            })
        } else {
            _isLoading.value = false
            _isError.value = true
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Complete the required fields"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Invalid email"
        } else {
            return true
        }
        return false
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unavailable service 😔"
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}