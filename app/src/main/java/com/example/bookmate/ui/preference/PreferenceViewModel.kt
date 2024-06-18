package com.example.bookmate.ui.preference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmate.data.UserRepository
import com.example.bookmate.data.request.PreferenceRequest
import com.example.bookmate.data.response.GenreResponse
import com.example.bookmate.data.response.PreferenceResponse
import com.example.bookmate.utils.getErrorMessageFromJson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferenceViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingSubmit = MutableLiveData<Boolean>()
    val isLoadingSubmit: LiveData<Boolean> = _isLoadingSubmit

    private val _listGenre = MutableLiveData<List<String>>()
    val listGenre: LiveData<List<String>> = _listGenre

    private val _selectedGenres = MutableLiveData<MutableList<String>>(mutableListOf())
    val selectedGenres: LiveData<MutableList<String>> = _selectedGenres

    private val _errorMessage = MutableLiveData<String>()

    private val _isErrorGetData = MutableLiveData<Boolean>()
    val isErrorGetData: LiveData<Boolean> = _isErrorGetData

    private val _isErrorSubmitData = MutableLiveData<Boolean>()
    val isErrorSubmitData: LiveData<Boolean> = _isErrorSubmitData

    fun getGenres() {
        _isLoading.value = true

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

    fun toggleGenreSelection(genre: String) {
        val selected = _selectedGenres.value ?: mutableListOf()
        if (selected.contains(genre)) {
            selected.remove(genre)
        } else {
            if (selected.size < 5) {
                selected.add(genre)
            }
        }
        _selectedGenres.value = selected

        Log.d("TOGGLE", _selectedGenres.value.toString())
    }

    fun submitGenres() {
        val selectedGenre = _selectedGenres.value

        if (selectedGenre != null && selectedGenre.size == 5) {
            _isLoadingSubmit.value = true
            val client =
                repository.getApiService().submitPreferences(PreferenceRequest(selectedGenre))

            client.enqueue(object : Callback<PreferenceResponse> {
                override fun onResponse(
                    call: Call<PreferenceResponse>, response: Response<PreferenceResponse>
                ) {
                    _isLoadingSubmit.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            viewModelScope.launch {
                                repository.setNewUserKey()
                            }

                            _errorMessage.value = ""
                            _isErrorSubmitData.value = false
                        } else {
                            _errorMessage.value = "Response null"
                            _isErrorSubmitData.value = true
                            Log.e(TAG, "Response null")
                        }
                    } else {
                        val errorMsg = getErrorMessageFromJson(response.errorBody()?.string())
                        _errorMessage.value = errorMsg
                        _isErrorSubmitData.value = true
                        Log.e(TAG, errorMsg)
                    }
                }

                override fun onFailure(call: Call<PreferenceResponse>, t: Throwable) {
                    _isLoadingSubmit.value = false
                    _errorMessage.value = "Unavailable service 😔"
                    _isErrorSubmitData.value = true
                    Log.e(TAG, "onFailure 2: ${t.message}")
                }
            })
        } else {
            Log.e(TAG, "Selected genres is less than 5")
            _errorMessage.value = "Choose 5 genres"
            _isErrorSubmitData.value = true
        }
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "PreferenceViewModel"
    }
}