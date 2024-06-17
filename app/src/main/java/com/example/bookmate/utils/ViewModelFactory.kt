package com.example.bookmate.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.data.UserRepository
import com.example.bookmate.di.Injection
import com.example.bookmate.ui.addReview.AddReviewViewModel
import com.example.bookmate.ui.bookdetail.BookDetailViewModel
import com.example.bookmate.ui.bookmark.BookmarkViewModel
import com.example.bookmate.ui.explore.ExploreViewModel
import com.example.bookmate.ui.explore.GenreViewModel
import com.example.bookmate.ui.home.HomeViewModel
import com.example.bookmate.ui.login.LoginViewModel
import com.example.bookmate.ui.main.MainViewModel
import com.example.bookmate.ui.preference.PreferenceViewModel
import com.example.bookmate.ui.profile.ProfileViewModel
import com.example.bookmate.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PreferenceViewModel::class.java) -> {
                PreferenceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookDetailViewModel::class.java) -> {
                BookDetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddReviewViewModel::class.java) -> {
                AddReviewViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
                ExploreViewModel(repository) as T
            }
            modelClass.isAssignableFrom(GenreViewModel::class.java) -> {
                GenreViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}