package com.example.bookmate.ui.preference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmate.data.UserRepository

class PreferenceViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listGenre = MutableLiveData<List<String>>()
    val listGenre: LiveData<List<String>> = _listGenre

    private val _selectedGenres = MutableLiveData<MutableList<String>>(mutableListOf())
    val selectedGenres: LiveData<MutableList<String>> = _selectedGenres

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

    fun isGenreSelected(genre: String): Boolean {
        return _selectedGenres.value?.contains(genre) == true
    }

    fun isMaxSelected(): Boolean {
        return _selectedGenres.value?.size == 5
    }

    companion object {
        private const val TAG = "PreferenceViewModel"
    }
}