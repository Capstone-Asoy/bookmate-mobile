package com.example.bookmate.ui.bookdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.request.AddBookmarkRequest
import com.example.bookmate.data.response.AddBookmarkResponse
import com.example.bookmate.data.response.BookDetailResponse
import com.example.bookmate.data.response.BookParcelize
import com.example.bookmate.data.response.DeleteBookmarkResponse
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

    private val _bookmarkMessage = MutableLiveData<String>()

    private val _isErrorAddBookmark = MutableLiveData<Boolean>()
    val isErrorAddBookmark: LiveData<Boolean> = _isErrorAddBookmark

    private val _isErrorDeleteBookmark = MutableLiveData<Boolean>()
    val isErrorDeleteBookmark: LiveData<Boolean> = _isErrorDeleteBookmark

    private val _bookDetail = MutableLiveData<BookDetailResponse>()
    val bookDetail: LiveData<BookDetailResponse> = _bookDetail

    private val _bookId = MutableLiveData<Int>()

    fun getBookDetail(id: Int) {
        _isLoading.value = true
        _bookId.value = id

        val client = repository.getApiService().getDetailBook(id)
        client.enqueue(object : Callback<BookDetailResponse> {
            override fun onResponse(
                call: Call<BookDetailResponse>, response: Response<BookDetailResponse>
            ) {
                _isLoading.value = false
                Log.d(TAG, response.toString())

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

    fun addBookmark() {
        _isLoading.value = true
        val bookId = _bookId.value
        if (bookId != null) {
            val client = repository.getApiService().addBookmark(AddBookmarkRequest(bookId))
            client.enqueue(object : Callback<AddBookmarkResponse> {
                override fun onResponse(
                    call: Call<AddBookmarkResponse>, response: Response<AddBookmarkResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _isErrorAddBookmark.value = false

                    } else {
                        val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                        _bookmarkMessage.value = errorMsg
                        _isErrorAddBookmark.value = true
                        Log.e(TAG, errorMsg)
                    }
                }

                override fun onFailure(call: Call<AddBookmarkResponse>, t: Throwable) {
                    _isLoading.value = false
                    _bookmarkMessage.value = "Unavailable service 😔"
                    _isErrorAddBookmark.value = true
                    Log.e(TAG, "onFailure 2: ${t.message}")
                }
            })
        }  else {
            _bookmarkMessage.value = "Unknown book id"
            _isErrorAddBookmark.value = true
        }
    }

    fun deleteBookmark() {
        _isLoading.value = true
        val bookId = _bookId.value
        if (bookId != null) {
            val client = repository.getApiService().deleteBookmark(bookId)
            client.enqueue(object : Callback<DeleteBookmarkResponse> {
                override fun onResponse(
                    call: Call<DeleteBookmarkResponse>, response: Response<DeleteBookmarkResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _isErrorDeleteBookmark.value = false

                    } else {
                        val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                        _bookmarkMessage.value = errorMsg
                        _isErrorDeleteBookmark.value = true
                        Log.e(TAG, errorMsg)
                    }
                }

                override fun onFailure(call: Call<DeleteBookmarkResponse>, t: Throwable) {
                    _isLoading.value = false
                    _bookmarkMessage.value = "Unavailable service 😔"
                    _isErrorDeleteBookmark.value = true
                    Log.e(TAG, "onFailure 2: ${t.message}")
                }
            })
        } else {
            _bookmarkMessage.value = "Unknown book id"
            _isErrorDeleteBookmark.value = true
        }
    }

    fun getBookParcelize(): BookParcelize? {
        val book = _bookDetail.value

        if (book != null) {
            return BookParcelize(book.bookId, book.title, book.author, book.coverImage)
        }
        return null
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    fun getBookmarkMessage(): String {
        return _bookmarkMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "BookDetailViewModel"
    }
}