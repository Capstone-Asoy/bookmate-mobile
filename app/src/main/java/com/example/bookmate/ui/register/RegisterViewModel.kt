package com.example.bookmate.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isMinimumLength = MutableLiveData<Boolean>()
    val isMinimumLength: LiveData<Boolean> = _isMinimumLength

    private val _isContainLowerCase = MutableLiveData<Boolean>()
    val isContainLowerCase: LiveData<Boolean> = _isContainLowerCase

    private val _isContainUpperCase = MutableLiveData<Boolean>()
    val isContainUpperCase: LiveData<Boolean> = _isContainUpperCase

    private val _isPasswordSame = MutableLiveData<Boolean>()
    val isPasswordSame: LiveData<Boolean> = _isPasswordSame

    private val _password = MutableLiveData<String>()
    private val _passwordConfirmation = MutableLiveData<String>()

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

    fun isMinimumLength(): Boolean {
        return _isMinimumLength.value ?: false
    }

    fun isContainUpper(): Boolean {
        return _isContainUpperCase.value ?: false
    }

    fun isContainLower(): Boolean {
        return _isContainLowerCase.value ?: false
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }

}