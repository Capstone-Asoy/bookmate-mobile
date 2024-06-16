package com.example.bookmate.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookmate.R
import com.example.bookmate.data.response.BookmarksItem
import com.example.bookmate.databinding.ItemBookmarkBinding

class BookmarkAdapter : ListAdapter<BookmarksItem, BookmarkAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class ViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookmarksItem) {
            binding.tvTitle.text = book.judul
            binding.tvAuthor.text = book.penulis
            Glide.with(binding.root.context)
                .load(book.image)
                .placeholder(R.drawable.default_cover)
                .into(binding.imgCover)
            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(book)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookmarksItem>() {
            override fun areItemsTheSame(oldItem: BookmarksItem, newItem: BookmarksItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BookmarksItem, newItem: BookmarksItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: BookmarksItem)
    }
}