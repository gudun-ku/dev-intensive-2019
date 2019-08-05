package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.R
import android.text.TextPaint
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.BitmapShader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import ru.skillbranch.devintensive.extensions.convertDpToPx
import ru.skillbranch.devintensive.utils.Utils


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : ImageView (context, attrs, defStyleAttr) {


    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
    }

    private var image: Bitmap? = null
    private var civDrawable: Drawable? = null
    private lateinit var clipPath: Path
    private lateinit var imagePaint: Paint
    private lateinit var borderPaint: Paint
    private lateinit var imageBounds: RectF
    private lateinit var borderBounds: RectF

    private lateinit var bitmapShader: Shader
    private var shaderMatrix = Matrix()
    private var initialized = false





    private var borderWidth =   DEFAULT_BORDER_WIDTH
    private var borderColor =   DEFAULT_BORDER_COLOR

    private var xmlImageResId = -1

    init {
        if(attrs!= null) {


            xmlImageResId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android",
                "src", -1)
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth,
               DEFAULT_BORDER_WIDTH)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            a.recycle()
        }

        imageBounds = RectF()
        borderBounds = RectF()
        clipPath = Path()


        imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = Utils.convertDpToPx(borderWidth.toFloat()).toFloat()
        }

        initialized = true
        setupImage()

    }

    private fun loadDrawable() {
        if (civDrawable == drawable) return

        civDrawable = drawable
        image = getBitmapFromDrawable(civDrawable)
        updateImageSize()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        setupImage()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupImage()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        setupImage()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        setupImage()
    }

    @Dimension
    fun getBorderWidth(): Int {
        return borderWidth
    }

    fun setBorderWidth(@Dimension dp:Int) {
        borderWidth = dp
        invalidate()
    }

    fun getBorderColor():Int {
       return borderColor
    }

    fun setBorderColor(hex:String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = resources.getColor(colorId)
        invalidate()
    }

    private fun drawBorder(canvas: Canvas) {
        if (borderPaint.strokeWidth > 0f) {
            canvas.drawOval(borderBounds,borderPaint)
        }
    }

    private fun drawImage(canvas: Canvas) {
       canvas.drawOval(imageBounds,imagePaint)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val strokeWidth = borderPaint.strokeWidth / 2f
        updateCircleDrawBounds(imageBounds)
        borderBounds.set(imageBounds)
        borderBounds.inset(strokeWidth ,strokeWidth)
        updateImageSize()
    }

    private fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        var left = (paddingLeft).toFloat()
        var top = (paddingTop).toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = Math.min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupImage() {
        if (!initialized) return

        if (civDrawable == drawable) return

        if (civDrawable != null) {
            civDrawable = drawable
            image = getBitmapFromDrawable(civDrawable)

        } else {
            if (xmlImageResId == 0) return
            civDrawable = AppCompatResources.getDrawable(context,xmlImageResId)
            image = getBitmapFromDrawable(civDrawable)
        }

        if (image == null) return

        bitmapShader = BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imagePaint.shader = bitmapShader
        updateImageSize()
    }


    private fun getBitmapFromDrawable(drawable:Drawable?):Bitmap? {
        if (drawable == null) return null

        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            else -> drawOnBitmap(drawable)
        }
    }

    private fun drawOnBitmap(drawable: Drawable):Bitmap {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
            drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun updateImageSize() {

        if (image == null) return

        val dx: Float
        val dy: Float
        val scale: Float

        // scale up/down with respect to this view size and maintain aspect ratio
        // translate bitmap position with dx/dy to the center of the image
        if (image!!.getWidth() < image!!.height) {
            scale = imageBounds.width() / image!!.width
            dx = imageBounds.left
            dy = imageBounds.top - image!!.height * scale / 2f + imageBounds.width() / 2f
        } else {
            scale = (imageBounds.height()) / image!!.height
            dx = imageBounds.left  - image!!.height * scale / 2f +
                    (imageBounds.width()) / 2f
            dy = imageBounds.top
        }
        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx, dy)
        bitmapShader.setLocalMatrix(shaderMatrix)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null ) return

        drawImage(canvas)
        drawBorder(canvas)
    }
}
