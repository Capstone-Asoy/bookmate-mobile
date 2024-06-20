package com.example.bookmate.ui.preference

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.example.bookmate.databinding.ActivityPreferenceBinding
import com.example.bookmate.ui.main.MainActivity
import com.example.bookmate.utils.ViewModelFactory

class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferenceBinding
    private lateinit var viewModel: PreferenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = obtainViewModel(this)

        setupAction()
        setupObserver()
    }

    private fun setupAction() {
        binding.btnNext.setOnClickListener {
            viewModel.submitGenres()
        }
    }

    private fun setupObserver() {
        viewModel.getGenres()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.isLoadingSubmit.observe(this) {
            showLoadingSubmit(it)
        }
        viewModel.listGenre.observe(this) {
            showGenreData(it)
        }
        viewModel.isErrorGetData.observe(this) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            }
        }
        viewModel.selectedGenres.observe(this) {
            binding.rvGenre.children.forEach { child ->
                val viewHolder = binding.rvGenre.getChildViewHolder(child)
                if (viewHolder is GenreAdapter.ViewHolder) {
                    viewHolder.setCheckboxEnabled(it.size < 5)
                }
            }
            (binding.rvGenre.adapter as? GenreAdapter)?.setSelectedGenres(it)
        }
        viewModel.isErrorSubmitData.observe(this) {
            if (it) {
                showToast(viewModel.getErrorMessage())
            } else {
                startActivity(Intent(this@PreferenceActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showGenreData(listGenre: List<String>) {
        val adapter = GenreAdapter()
        adapter.submitList(listGenre)
        adapter.setOnItemClickCallback(object : GenreAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                viewModel.toggleGenreSelection(data)
            }
        })
        binding.rvGenre.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingSubmit(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarSubmit.visibility = View.VISIBLE
            binding.btnNext.isEnabled = false
        } else {
            binding.progressBarSubmit.visibility = View.GONE
            binding.btnNext.isEnabled = true
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): PreferenceViewModel {
        val factory = ViewModelFactory.getInstance(this@PreferenceActivity)
        return ViewModelProvider(activity, factory)[PreferenceViewModel::class.java]
    }
}