package com.example.bookmate.ui.bookdetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.R
import com.example.bookmate.databinding.ActivityMainBinding
import com.example.bookmate.ui.main.MainViewModel
import com.example.bookmate.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class BookDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.review_nav)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_bookmark -> {
                    // Tindakan untuk memasukkan buku ke favorit
                    true
                }
                R.id.action_review -> {
                    // Tindakan untuk membuka halaman review buku
                    true
                }
                else -> false
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): BookViewModel {
        val factory = ViewModelFactory.getInstance(this@BookDetailActivity)
        return ViewModelProvider(activity, factory)[BookViewModel::class.java]
    }
}
