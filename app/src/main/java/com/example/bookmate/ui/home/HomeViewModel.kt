package com.example.bookmate.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.data.response.RecommendationResponse
import com.example.bookmate.data.response.SearchResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorGetData = MutableLiveData<Boolean>()
    val isErrorGetData: LiveData<Boolean> = _isErrorGetData

    private val _pageTitle = MutableLiveData<String>().apply {
        value = "For you"
    }
    val pageTitle: LiveData<String> = _pageTitle

    private val _errorMessage = MutableLiveData<String>()

    private val _listBuku = MutableLiveData<List<BookItem>>()
    val listBuku: LiveData<List<BookItem>> = _listBuku

    private val _listBuku2 = MutableLiveData<List<BookItem>>()
    val listBuku2: LiveData<List<BookItem>> = _listBuku2

    private val _listBukuFromHistory =
        MutableLiveData<List<BookItem>>().apply { value = emptyList() }
    val listBukuFromHistory: LiveData<List<BookItem>> = _listBukuFromHistory

    private val _listBukuFromBookmark =
        MutableLiveData<List<BookItem>>().apply { value = emptyList() }
    val listBukuFromBookmark: LiveData<List<BookItem>> = _listBukuFromBookmark

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
                        _listBuku.value = responseBody.recommendation.subList(
                            0,
                            responseBody.recommendation.size / 2
                        )
                        _listBuku2.value = responseBody.recommendation.subList(
                            responseBody.recommendation.size / 2 + 1,
                            responseBody.recommendation.size - 1
                        )

                        responseBody.basedOnHistory?.let {
                            _listBukuFromHistory.value = responseBody.basedOnHistory
                        }
                        responseBody.bookmark?.let {
                            _listBukuFromBookmark.value = responseBody.bookmark
                        }

                        _errorMessage.value = ""
                        _isErrorGetData.value = false
                        _pageTitle.value = "For you"
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

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unavailable service 😔"
                _isErrorGetData.value = true
                Log.e(TAG, "onFailure 2: ${t.message}")
            }
        })
    }

    fun search(keyword: String) {
        _isLoading.value = true

        val client = repository.getApiService().search(keyword)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>, response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listBuku.value = responseBody.results

                        _pageTitle.value = "Search result '${keyword}'"
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

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
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

    fun getListBookFromHistory(): List<BookItem> {
        return _listBukuFromHistory.value ?: emptyList()
    }

    fun getListBookFromBookmark(): List<BookItem> {
        return _listBukuFromBookmark.value ?: emptyList()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}