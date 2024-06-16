package com.example.bookmate.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.response.BookmarksItem
import com.example.bookmate.data.response.BookmarkResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()

    private val _listBookmark = MutableLiveData<List<BookmarksItem>>()
    val listBookmark: LiveData<List<BookmarksItem>> = _listBookmark

    fun getBookmarks() {
        _isLoading.value = true

        val client = repository.getApiService().getBookmarks()
        client.enqueue(object : Callback<BookmarkResponse> {
            override fun onResponse(
                call: Call<BookmarkResponse>, response: Response<BookmarkResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listBookmark.value = responseBody.bookmarks

                        _errorMessage.value = ""
                        _isError.value = false
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

            override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
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


    companion object {
        private const val TAG = "BookmarkViewModel"
    }
}