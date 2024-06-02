package com.example.bookmate.ui.welcome

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.databinding.ActivityWelcomeBinding
import com.example.bookmate.utils.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)
    }

    private fun obtainViewModel(activity: AppCompatActivity): WelcomeViewModel {
        val factory = ViewModelFactory.getInstance(this@WelcomeActivity)
        return ViewModelProvider(activity, factory)[WelcomeViewModel::class.java]
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
    }
}