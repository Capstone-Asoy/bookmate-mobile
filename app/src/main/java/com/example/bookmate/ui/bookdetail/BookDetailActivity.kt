package com.example.bookmate.ui.bookdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookmate.R
import com.example.bookmate.data.response.BookDetailResponse
import com.example.bookmate.databinding.ActivityBookDetailBinding
import com.example.bookmate.ui.addReview.AddReviewActivity
import com.example.bookmate.utils.ViewModelFactory

class BookDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: BookDetailViewModel
    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        setupAction()
        setupObserver()
    }

    private fun setupAction() {
        binding.checkboxBookmark.setOnClickListener {
            if (binding.checkboxBookmark.isChecked) {
                viewModel.addBookmark()
            } else {
                viewModel.deleteBookmark()
            }
        }

        binding.btnGiveReview.setOnClickListener {
            val book = viewModel.getBookParcelize()
            book?.let {
                val intent = Intent(this, AddReviewActivity::class.java)
                intent.putExtra(AddReviewActivity.EXTRA_BOOK, book)
                startActivity(intent)
            } ?: run {
                showToast("Couldn't add review now. Try again later")
            }
        }
    }

    private fun setupObserver() {
        val bookId = intent.getIntExtra(EXTRA_ID, -1)

        if (bookId != -1) {
            viewModel.getBookDetail(bookId)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.isErrorGetData.observe(this) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
        viewModel.bookDetail.observe(this) {
            showData(it)
        }
        viewModel.isErrorAddBookmark.observe(this) {
            if (it) {
                showToast(viewModel.getBookmarkMessage())
                binding.checkboxBookmark.isChecked = false
            } else {
                showToast("Added to bookmark")
            }
        }
        viewModel.isErrorDeleteBookmark.observe(this) {
            if (it) {
                showToast(viewModel.getBookmarkMessage())
                binding.checkboxBookmark.isChecked = true
            } else {
                showToast("Deleted from bookmark")
            }
        }
    }

    private fun showData(book: BookDetailResponse) {
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = getString(R.string.by_author, book.author)
        binding.tvRating.text = book.avgRating
        binding.ratingStar.setRatingValue(book.avgRating.toDouble().toInt())
        binding.information.tvSynopsis.text = book.synopsis
        binding.information.tvPublisherValue.text = book.publisher
        binding.information.tvPublishedYearValue.text = book.year.toString()
        binding.information.tvPagesValue.text = book.pageCount.toString()
        binding.information.tvIsbnValue.text = book.isbn
        binding.information.tvGenreValue.text = book.genre.toString()
        binding.checkboxBookmark.isChecked = book.isBookmarked
        Glide.with(binding.root.context)
            .load(book.coverImage)
            .placeholder(R.drawable.default_cover)
            .into(binding.imgCoverBg)
        Glide.with(binding.root.context)
            .load(book.coverImage)
            .placeholder(R.drawable.default_cover)
            .into(binding.imgCover)

        if (book.reviews.isNotEmpty()) {
            binding.information.tvNoReview.visibility = View.GONE
            binding.information.rvReview.visibility = View.VISIBLE

            val adapter = ReviewAdapter()
            adapter.submitList(book.reviews)
            binding.information.rvReview.adapter = adapter
        } else {
            binding.information.tvNoReview.visibility = View.VISIBLE
            binding.information.rvReview.visibility = View.GONE
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): BookDetailViewModel {
        val factory = ViewModelFactory.getInstance(this@BookDetailActivity)
        return ViewModelProvider(activity, factory)[BookDetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
