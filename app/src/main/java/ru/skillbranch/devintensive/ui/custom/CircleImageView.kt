package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.R


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : ImageView (context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2.0f
        private const val DEFAULT_BORDER_COLOR = Color.MAGENTA
    }

    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var borderColor =  DEFAULT_BORDER_COLOR

    init {
        if(attrs!= null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
           // borderWidth = a.getFloat(R.styleable.CircleImageView_borderWidth, DEFAULT_BORDER_WIDTH)
            borderColor = a.getColor(R.styleable.CircleImageView_borderColor, DEFAULT_BORDER_COLOR)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
