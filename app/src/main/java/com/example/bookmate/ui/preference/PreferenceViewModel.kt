package com.example.bookmate.ui.preference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmate.data.UserRepository
import kotlinx.coroutines.launch

class PreferenceViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listGenre = MutableLiveData<List<String>>()
    val listGenre: LiveData<List<String>> = _listGenre

    private val _selectedGenres = MutableLiveData<MutableList<String>>(mutableListOf())
    val selectedGenres: LiveData<MutableList<String>> = _selectedGenres

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getGenres() {
        _isLoading.value = true

        android.os.Handler().postDelayed({
            val listOfGenres = listOf(
                "Fiksi",
                "Non-Fiksi",
                "Biografi",
                "Autobiografi",
                "Sejarah",
                "Sains",
                "Filsafat",
                "Agama",
                "Petualangan",
                "Fantasi",
                "Fiksi Ilmiah",
                "Misteri",
                "Thriller",
                "Horror",
                "Romansa",
                "Komedi",
                "Drama",
                "Puisi",
                "Self-Help",
                "Pengembangan Diri",
                "Psikologi",
                "Buku Anak-Anak",
                "Young Adult",
                "Grafis Novel",
                "Seni",
                "Musik",
                "Travel",
                "Kuliner"
            )

            _isLoading.value = false
            _listGenre.value = listOfGenres
        }, 1500)
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
            viewModelScope.launch {
                repository.setNewUserKey()
            }
            _isError.value = false
        } else {
            Log.e(TAG, "Selected genres is less than 5")
            _errorMessage.value = "Choose 5 genres"
            _isError.value = true
        }
    }

    fun getErrorMessage(): String {
        return _errorMessage.value ?: "Unknown error"
    }

    companion object {
        private const val TAG = "PreferenceViewModel"
    }
}