package com.example.bookmate.ui.register

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmate.BuildConfig
import com.example.bookmate.R
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.pref.UserModel
import com.example.bookmate.data.response.RegisterResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import com.example.bookmate.utils.reduceFileImage
import com.example.bookmate.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

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

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun updateImage(uriParam: Uri) {
        _uri.value = uriParam
    }


    fun register(name: String, email: String, context: Context) {
        _isLoading.value = true

        if (validateValues(name, email)) {

            val nameBody = name.toRequestBody("text/plain".toMediaType())
            val emailBody = email.toRequestBody("text/plain".toMediaType())
            val passwordBody = _password.value?.toRequestBody("text/plain".toMediaType())
            var imageFile: File? = null
            var multipartBody: MultipartBody.Part? = null

            val uriValue = _uri.value
            if (uriValue != null) {
                imageFile = uriToFile(uriValue, context).reduceFileImage()
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                multipartBody = MultipartBody.Part.createFormData(
                    "image", imageFile.name, requestImageFile
                )
            }

            val client = repository.getApiService()
                .register(nameBody, emailBody, passwordBody!!, multipartBody)

            client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>, response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val imageFileName =
                                if (imageFile != null) imageFile.name else context.getString(R.string.no_image)
                            val user =
                                generateUserModel(
                                    name,
                                    email,
                                    responseBody.token,
                                    imageFileName,
                                    context
                                )

                            saveSession(user)
                            _errorMessage.value = ""
                            _isError.value = false
                        } else {
                            _errorMessage.value = "Null response"
                            _isError.value = true
                        }
                    } else {
                        val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                        _errorMessage.value = errorMsg
                        _isError.value = true
                        Log.e(TAG, errorMsg)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                    _errorMessage.value = t.message ?: "Register failed"
                    Log.e(TAG, "onFailure 2: ${t.message}")
                }
            })
        } else {
            _isError.value = true
        }
        _isLoading.value = false
    }

    private fun validateValues(name: String, email: String): Boolean {
        if (name.isEmpty()) {
            _errorMessage.value = "Name is required"
        } else if (email.isEmpty()) {
            _errorMessage.value = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Invalid email"
        } else if (_isMinimumLength.value != true || _isContainLowerCase.value != true || _isContainUpperCase.value != true || _isPasswordSame.value != true) {
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

    private fun generateUserModel(
        name: String, email: String, token: String, imageFileName: String, context: Context
    ): UserModel {
        val photoUrl =
            if (imageFileName != context.getString(R.string.no_image)) BuildConfig.IMAGE_URL + name + "-" + imageFileName else ""

        return UserModel(name, email, token, photoUrl, isLogin = true, isNewUser = true)
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}