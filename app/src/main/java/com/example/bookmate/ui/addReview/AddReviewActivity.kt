package com.example.bookmate.ui.addReview

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookmate.R
import com.example.bookmate.data.response.BookParcelize
import com.example.bookmate.databinding.ActivityAddReviewBinding
import com.example.bookmate.utils.ViewModelFactory

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var viewModel: AddReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        setupAction()
        setupObserver()
        setupData()
    }

    private fun setupData() {
        val book = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_BOOK, BookParcelize::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_BOOK)
        }

        if (book != null) {
            viewModel.setBookData(book)
        }
    }

    private fun setupAction() {
        binding.btnSendNext.setOnClickListener {
            val review = binding.edReview.editText?.text.toString()
            val rating = binding.checkboxRating.getRating()

            if (rating == 0) {
                showToast("Give your rating!")
            } else if (review.isBlank()) {
                showToast("Give your review description")
            } else {
                viewModel.sendReview(rating, review)
            }
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.bookData.observe(this) {
            Glide.with(binding.root.context)
                .load(it.coverImage)
                .placeholder(R.drawable.default_cover)
                .into(binding.imgCover)
            binding.tvTitle.text = it.title
            binding.tvAuthor.text = it.author
        }
        viewModel.isError.observe(this) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            } else {
                showToast("Review added, thank you!")
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSendNext.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnSendNext.isEnabled = true
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddReviewViewModel {
        val factory = ViewModelFactory.getInstance(this)
        return ViewModelProvider(activity, factory)[AddReviewViewModel::class.java]
    }

    companion object {
        const val EXTRA_BOOK = "extra_book"
    }
}