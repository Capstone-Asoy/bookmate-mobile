package com.example.bookmate.ui.bookdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmate.data.response.ReviewsItem
import com.example.bookmate.databinding.ItemReviewBinding
import com.example.bookmate.utils.formatDate

class ReviewAdapter : ListAdapter<ReviewsItem, ReviewAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewsItem) {
            binding.tvReviewer.text = review.userName
            binding.tvReviewDate.text = formatDate(review.date)
            binding.ratingStar.setRatingValue(review.rating.toInt())
            binding.tvReviewDesc.text = review.review
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewsItem>() {
            override fun areItemsTheSame(oldItem: ReviewsItem, newItem: ReviewsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ReviewsItem, newItem: ReviewsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}