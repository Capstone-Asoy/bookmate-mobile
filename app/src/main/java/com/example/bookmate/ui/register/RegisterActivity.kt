package com.example.bookmate.ui.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.R
import com.example.bookmate.databinding.ActivityRegisterBinding
import com.example.bookmate.ui.login.LoginActivity
import com.example.bookmate.ui.main.MainActivity
import com.example.bookmate.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        viewModel = obtainViewModel(this@RegisterActivity)
        setupAction()
        setupObserver()
    }


    private fun setupAction() {
        binding.btnChooseImage.setOnClickListener {
            startGallery()
        }
        binding.edPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.checkPassword(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edConfirmPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.checkPasswordConfirmation(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.btnSubmit.setOnClickListener {
            val name = binding.edName.editText?.text.toString()
            val email = binding.edEmail.editText?.text.toString()
            viewModel.register(name, email, this)
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.uri.observe(this) {
            showImage(it)
        }
        viewModel.isMinimumLength.observe(this) {
            showMinimumLengthMsg(it)
        }

        viewModel.isContainLowerCase.observe(this) {
            showContainLowerMsg(it)
        }

        viewModel.isContainUpperCase.observe(this) {
            showContainUpperMsg(it)
        }

        viewModel.isPasswordSame.observe(this) {
            showDoesntMatchMsg(it)
        }
        viewModel.isError.observe(this) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            } else {
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                showToast(getString(R.string.register_success))
                finish()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(uri: Uri) {
        binding.imgProfile.setImageURI(uri)
    }

    private fun showMinimumLengthMsg(isMinimumLength: Boolean) {
        binding.icPasswordCheck.visibility = if (isMinimumLength) View.VISIBLE else View.GONE
        binding.icPasswordX.visibility = if (isMinimumLength) View.GONE else View.VISIBLE
        binding.tvMinimumLength.setTextColor(getTextColor(isMinimumLength))
    }

    private fun showContainLowerMsg(isContainLower: Boolean) {
        binding.icPasswordCheckLower.visibility = if (isContainLower) View.VISIBLE else View.GONE
        binding.icPasswordXLower.visibility = if (isContainLower) View.GONE else View.VISIBLE
        binding.tvContainLower.setTextColor(getTextColor(isContainLower))
    }

    private fun showContainUpperMsg(isContainUpper: Boolean) {
        binding.icPasswordCheckUpper.visibility = if (isContainUpper) View.VISIBLE else View.GONE
        binding.icPasswordXUpper.visibility = if (isContainUpper) View.GONE else View.VISIBLE
        binding.tvContainUpper.setTextColor(getTextColor(isContainUpper))
    }

    private fun showDoesntMatchMsg(isPasswordMatch: Boolean) {
        binding.msgPasswordSame.visibility = if (isPasswordMatch) View.GONE else View.VISIBLE
    }

    private fun getTextColor(bool: Boolean): Int {
        val colorResId = if (bool) R.color.orange_400 else R.color.red_600
        return ContextCompat.getColor(this, colorResId)
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

    private fun obtainViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(this@RegisterActivity)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateImage(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
}