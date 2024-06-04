package com.example.bookmate.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        binding.btnSubmit.setOnClickListener {
            val email = binding.edEmail.editText?.text.toString()
            val password = binding.edPassword.editText?.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.isError.observe(this) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSubmit.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnSubmit.isEnabled = true
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(this@LoginActivity)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }
}