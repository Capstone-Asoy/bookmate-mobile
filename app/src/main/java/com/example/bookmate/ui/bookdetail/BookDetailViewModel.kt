package com.example.bookmate.ui.bookdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.response.BookDetailResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()

    private val _isErrorGetData = MutableLiveData<Boolean>()
    val isErrorGetData: LiveData<Boolean> = _isErrorGetData

    private val _bookDetail = MutableLiveData<BookDetailResponse>()
    val bookDetail: LiveData<BookDetailResponse> = _bookDetail

    fun getBookDetail(id: Int) {
        _isLoading.value = true

        val client = repository.getApiService().getDetailBook(id)
        client.enqueue(object : Callback<BookDetailResponse> {
            override fun onResponse(
                call: Call<BookDetailResponse>, response: Response<BookDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _bookDetail.value = responseBody!!

                        _errorMessage.value = ""
                        _isErrorGetData.value = false
                    } else {
                        _errorMessage.value = "Failed to get data. Try again later"
                        _isErrorGetData.value = true
                        Log.e(TAG, "Response null")
                    }
                } else {
                    val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                    _errorMessage.value = errorMsg
                    _isErrorGetData.value = true
                    Log.e(TAG, errorMsg)
                }
            }

            override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unavailable service 😔"
                _isErrorGetData.value = true
                Log.e(TAG, "onFailure 2: ${t.message}")
            }
        })
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "BookDetailViewModel"
    }
}