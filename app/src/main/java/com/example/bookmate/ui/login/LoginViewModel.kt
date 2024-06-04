package com.example.bookmate.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun login(email: String, password: String) {
        _isLoading.value = true
        android.os.Handler().postDelayed({
            if (validateInput(email, password)) {
                _errorMessage.value = ""
                _isError.value = false
                _isLoading.value = false

                saveSession(getUserDummy())

            } else {
                _isError.value = true
                _isLoading.value = false
            }
        }, 2000)
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
        return _errorMessage.value ?: "Unknown error"
    }

    private fun getUserDummy(): UserModel {
        return UserModel("Fachry", "fachry@gmail.com", "token", true)
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}