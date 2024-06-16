package com.example.bookmate.components

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.bookmate.R

class RoundedBottomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var path: Path = Path()
    private var overlayPaint: Paint = Paint()

    init {
        overlayPaint.color = ContextCompat.getColor(context, R.color.overlay_black)
        overlayPaint.style = Paint.Style.FILL
        overlayPaint.maskFilter = BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        val radius = h.toFloat() / 8
        path.addRoundRect(rect, floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius), Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(path)
        super.onDraw(canvas)
        canvas.drawPath(path, overlayPaint)
        canvas.restore()
    }
}