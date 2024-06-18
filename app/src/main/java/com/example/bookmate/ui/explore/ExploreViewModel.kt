package com.example.bookmate.ui.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.response.BookItem
import com.example.bookmate.data.response.GenreResponse
import com.example.bookmate.data.response.SearchResponse
import com.example.bookmate.ui.home.HomeViewModel
import com.example.bookmate.ui.preference.PreferenceViewModel
import com.example.bookmate.utils.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorGetData = MutableLiveData<Boolean>()
    val isErrorGetData: LiveData<Boolean> = _isErrorGetData

    private val _errorMessage = MutableLiveData<String>()

    private val _listGenre = MutableLiveData<List<String>>()
    val listGenre: LiveData<List<String>> = _listGenre

    private val _listBuku = MutableLiveData<List<BookItem>>()
    val listBuku: LiveData<List<BookItem>> = _listBuku

    private val _isErrorSearch = MutableLiveData<Boolean>()
    val isErrorSearch: LiveData<Boolean> = _isErrorSearch

    private val _isShowTabs = MutableLiveData<Boolean>()
    val isShowTabs: LiveData<Boolean> = _isShowTabs

    fun getGenres() {
        _isLoading.value = true
        _isShowTabs.value = true

        val client = repository.getApiService().getGenres()
        client.enqueue(object : Callback<GenreResponse> {
            override fun onResponse(
                call: Call<GenreResponse>, response: Response<GenreResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listGenre.value = responseBody.genres

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

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unavailable service 😔"
                _isErrorGetData.value = true
                Log.e(TAG, "onFailure 2: ${t.message}")
            }
        })
    }

    fun search(keyword: String) {
        _isLoading.value = true
        _isShowTabs.value = false

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

                        _errorMessage.value = ""
                        _isErrorSearch.value = false
                    } else {
                        _errorMessage.value = "Failed to get data. Try again later"
                        _isErrorSearch.value = true
                        Log.e(TAG, "Response null")
                    }
                } else {
                    val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                    _errorMessage.value = errorMsg
                    _isErrorSearch.value = true
                    Log.e(TAG, errorMsg)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unavailable service 😔"
                _isErrorSearch.value = true
                Log.e(TAG, "onFailure 2: ${t.message}")
            }
        })
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "FragmentViewModel"
    }
}