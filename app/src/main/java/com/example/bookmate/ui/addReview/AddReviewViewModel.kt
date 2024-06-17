package com.example.bookmate.ui.addReview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.request.AddReviewRequest
import com.example.bookmate.data.response.AddReviewResponse
import com.example.bookmate.data.response.BookParcelize
import com.example.bookmate.utils.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()

    private val _bookData = MutableLiveData<BookParcelize>()
    val bookData: LiveData<BookParcelize> = _bookData

    fun setBookData(book: BookParcelize) {
        _bookData.value = book
    }

    fun sendReview(rating: Int, review: String) {
        _isLoading.value = true
        val data = AddReviewRequest(rating, review)
        val book = _bookData.value

        if (book != null) {
            val client = repository.getApiService().addReview(book.bookId, data)
            client.enqueue(object : Callback<AddReviewResponse> {
                override fun onResponse(
                    call: Call<AddReviewResponse>, response: Response<AddReviewResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _isError.value = false

                    } else {
                        val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                        _errorMessage.value = errorMsg
                        _isError.value = true
                        Log.e(TAG, errorMsg)
                    }
                }

                override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Unavailable service 😔"
                    _isError.value = true
                    Log.e(TAG, "onFailure 2: ${t.message}")
                }
            })
        } else {
            _errorMessage.value = "Couldn't get book id. Try again later"
            _isError.value = true
        }
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "AddReviewViewModel"
    }
}