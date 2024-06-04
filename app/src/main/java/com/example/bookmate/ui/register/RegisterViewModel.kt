package com.example.bookmate.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isMinimumLength = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isMinimumLength: LiveData<Boolean> = _isMinimumLength

    private val _isContainLowerCase = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isContainLowerCase: LiveData<Boolean> = _isContainLowerCase

    private val _isContainUpperCase = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isContainUpperCase: LiveData<Boolean> = _isContainUpperCase

    private val _isPasswordSame = MutableLiveData<Boolean>()
    val isPasswordSame: LiveData<Boolean> = _isPasswordSame

    private val _password = MutableLiveData<String>()
    private val _passwordConfirmation = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError


    fun register(name: String, email: String) {
        _isLoading.value = true

        android.os.Handler().postDelayed({
            if (validateValues(name, email)) {
                _errorMessage.value = ""
                _isError.value = false
            } else {
                _isError.value = true
            }
            _isLoading.value = false
        }, 2000)
    }

    private fun validateValues(name: String, email: String): Boolean {
        if (name.isEmpty()) {
            _errorMessage.value = "Name is required"
        } else if (email.isEmpty()) {
            _errorMessage.value = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Invalid email"
        } else if (_isMinimumLength.value != true
            || _isContainLowerCase.value != true
            || _isContainUpperCase.value != true
            || _isPasswordSame.value != true
        ) {
            _errorMessage.value = "The password does not meet the criteria"
        } else {
            return true
        }
        return false
    }

    fun checkPassword(str: CharSequence) {
        _isMinimumLength.value = str.length >= 8
        var isContainLower = false
        var isContainUpper = false

        for (char in str) {
            if (char.isLowerCase()) isContainLower = true
            else if (char.isUpperCase()) isContainUpper = true
        }

        _isContainLowerCase.value = isContainLower
        _isContainUpperCase.value = isContainUpper

        _password.value = str.toString()
        checkPasswordSame()
    }

    fun checkPasswordConfirmation(str: CharSequence) {
        _passwordConfirmation.value = str.toString()
        checkPasswordSame()
    }

    private fun checkPasswordSame() {
        _isPasswordSame.value = _password.value.equals(_passwordConfirmation.value)
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}