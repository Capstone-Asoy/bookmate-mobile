package com.example.bookmate.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.data.response.RecommendationResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isAddSpacing = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isAddSpacing: LiveData<Boolean> = _isAddSpacing

    private val _errorMessage = MutableLiveData<String>()

    private val _listBuku = MutableLiveData<List<BookItem>>()
    val listBuku: LiveData<List<BookItem>> = _listBuku

    fun getData() {
        _isLoading.value = true

        val client = repository.getApiService().getRekomendasi()
        client.enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>, response: Response<RecommendationResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listBuku.value = responseBody.data

                        _errorMessage.value = ""
                        _isError.value = false
                        _isAddSpacing.value = true
                    } else {
                        _errorMessage.value = "Failed to get data. Try again later"
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

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unavailable service 😔"
                _isError.value = true
                Log.e(TAG, "onFailure 2: ${t.message}")
            }
        })
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    fun isAddSpacing(): Boolean {
        return _isAddSpacing.value ?: true
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}