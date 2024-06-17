package com.example.bookmate.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.bookmate.R

class CheckboxRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var rating: Int = 0
    private val checkboxes = mutableListOf<CheckBox>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_checkbox_rating, this, true)
        orientation = HORIZONTAL
        for (i in 0 until 5) {
            val checkBox = CheckBox(context).apply {
                buttonDrawable =
                    ContextCompat.getDrawable(context, R.drawable.custom_checkbox_selector)
                background = ContextCompat.getDrawable(context, R.drawable.checkbox_star_background)
                setOnClickListener {
                    setRating(i + 1)
                }
            }
            val layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, if (i < 4) 8 else 0, 0)
            }
            checkBox.layoutParams = layoutParams
            checkboxes.add(checkBox)
            findViewById<LinearLayout>(R.id.linearLayoutCheckboxes).addView(checkBox)
        }
    }

    private fun setRating(rating: Int) {
        this.rating = rating.coerceIn(0, 5)
        updateCheckboxes()
    }

    private fun updateCheckboxes() {
        for (i in 0 until 5) {
            checkboxes[i].setOnCheckedChangeListener(null)
            checkboxes[i].isChecked = i < rating
            checkboxes[i].setOnClickListener {
                setRating(i + 1)
            }
        }
    }

    fun getRating(): Int {
        return rating
    }

    fun setRatingValue(rating: Int) {
        setRating(rating)
    }
}