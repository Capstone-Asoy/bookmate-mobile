package com.example.bookmate.ui.addReview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.response.BookParcelize

class AddReviewViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()

    private val _bookData = MutableLiveData<BookParcelize>()
    val bookData: LiveData<BookParcelize> = _bookData

    fun setBookData(book: BookParcelize) {
        _bookData.value = book
    }

    fun sendReview(rating: Int, review: String) {

    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "AddReviewViewModel"
    }
}