package com.example.bookmate.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.bookmate.R

class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var ratingValue: Int = 0
    private val starViews = mutableListOf<ImageView>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_rating, this, true)
        orientation = HORIZONTAL
        for (i in 0 until 5) {
            val star = ImageView(context)
            star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_24))
            starViews.add(star)
            findViewById<LinearLayout>(R.id.linearLayoutStars).addView(star)
        }
    }

    fun setRatingValue(rating: Int) {
        ratingValue = rating.coerceIn(0, 5)
        updateStars()
    }

    private fun updateStars() {
        for (i in 0 until 5) {
            if (i < ratingValue) {
                starViews[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_star_filled_24
                    )
                )
            } else {
                starViews[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_star_24
                    )
                )
            }
        }
    }
}