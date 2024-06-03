package com.example.bookmate.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.databinding.ActivityLoginBinding
import com.example.bookmate.ui.register.RegisterActivity
import com.example.bookmate.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)
        enableEdgeToEdge()

        setupAction()
        setupObserver()
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setupObserver() {

    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(this@LoginActivity)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }
}