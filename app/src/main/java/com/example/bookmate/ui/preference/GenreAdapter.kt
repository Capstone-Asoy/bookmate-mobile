package com.example.bookmate.ui.preference

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmate.databinding.ItemGenreBinding

class GenreAdapter: ListAdapter<String, GenreAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var selectedGenres: List<String> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedGenres(genres: List<String>) {
        selectedGenres = genres
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = getItem(position)
        val isChecked = selectedGenres.contains(genre)

        holder.bind(genre, isChecked)
    }

    inner class ViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: String, isChecked: Boolean) {
            binding.checkbox.text = genre
            binding.checkbox.isChecked = isChecked
            binding.checkbox.setOnClickListener {
                onItemClickCallback.onItemClicked(genre)
            }
        }

        fun setCheckboxEnabled(lessThan5: Boolean) {
            if (!lessThan5) {
                binding.checkbox.isEnabled = binding.checkbox.isChecked
            } else {
                binding.checkbox.isEnabled = true
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String, newItem: String
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}