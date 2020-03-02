package ru.skillbranch.devintensive.utils

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import kotlin.math.absoluteValue

object Utils {

    private fun removeWhiteSpaces(str: String?): String? = when(str) {
        null -> null
        else -> str.trim()?.replace("\\s+".toRegex(), " ")
    }

    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val preparedFullName = removeWhiteSpaces(fullName)
        val parts : List<String>? = preparedFullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)
        firstName = if (firstName.isNullOrBlank()) null else  firstName
        lastName = if (lastName.isNullOrBlank()) null else  lastName

        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstInitial = firstName?.getOrNull(0)
        val secondInitial  = lastName?.getOrNull(0)
        val initials:StringBuilder?
        initials = when {
           firstInitial == null && secondInitial == null -> null
           secondInitial == null -> StringBuilder().append(firstInitial)
           firstInitial == null -> StringBuilder().append(secondInitial)
           else -> StringBuilder().append(firstInitial).append(secondInitial)
        }
        return when(initials) {
            null -> null
            else -> if (initials.toString().isBlank()) null else initials.toString().toUpperCase()
        }
    }


    fun transliteration(payload: String, divider: String = " "): String {
        val fullName: List<String> = payload.split(" ")
        var res = ""
        for (word: String in fullName) {
            var sb = ""
            val charArray = word.toCharArray()
            for (ch: Char in charArray.iterator()) {
                val char = when (ch) {
                    'а' -> "a"
                    'б' -> "b"
                    'в' -> "v"
                    'г' -> "g"
                    'д' -> "d"
                    'е' -> "e"
                    'ё' -> "e"
                    'ж' -> "zh"
                    'з' -> "z"
                    'и' -> "i"
                    'й' -> "i"
                    'к' -> "k"
                    'л' -> "l"
                    'м' -> "m"
                    'н' -> "n"
                    'о' -> "o"
                    'п' -> "p"
                    'р' -> "r"
                    'с' -> "s"
                    'т' -> "t"
                    'у' -> "u"
                    'ф' -> "f"
                    'х' -> "h"
                    'ц' -> "c"
                    'ч' -> "ch"
                    'ш' -> "sh"
                    'щ' -> "sh'"
                    'ъ' -> ""
                    'ы' -> "i"
                    'ь' -> ""
                    'э' -> "e"
                    'ю' -> "yu"
                    'я' -> "ya"

                    'А' -> "A"
                    'Б' -> "B"
                    'В' -> "V"
                    'Г' -> "G"
                    'Д' -> "D"
                    'Е' -> "E"
                    'Ё' -> "E"
                    'Ж' -> "Zh"
                    'З' -> "Z"
                    'И' -> "I"
                    'Й' -> "I"
                    'К' -> "K"
                    'Л' -> "L"
                    'М' -> "M"
                    'Н' -> "N"
                    'О' -> "O"
                    'П' -> "P"
                    'Р' -> "R"
                    'С' -> "S"
                    'Т' -> "T"
                    'У' -> "U"
                    'Ф' -> "F"
                    'Х' -> "H"
                    'Ц' -> "C"
                    'Ч' -> "Ch"
                    'Ш' -> "Sh"
                    'Щ' -> "Sh'"
                    'Ъ' -> ""
                    'Ы' -> "I"
                    'Ь' -> ""
                    'Э' -> "E"
                    'Ю' -> "Yu"
                    'Я' -> "Ya"
                    else -> ch.toString()
                }
                sb = "$sb$char"
            }
            if (!res.isNullOrBlank() and res.isNotEmpty() and sb.isNotEmpty()) res = "$res$divider$sb"
            else res = "$res$sb"
        }

        return res
    }


    fun convertDpToPx(dp: Float): Int{
        return  (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun convertPxToDp(px: Int): Float {
        val density =  Resources.getSystem().displayMetrics.density
        return if (density == 0f) px.toFloat() else (px / density)
    }

    fun convertSpToPx(sp: Float): Int {

        return (sp * Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }

    fun isGithubUrlValid(url: String): Boolean {

        var subUrl = url
        val afterLast = url.substringAfterLast("/")
        if (afterLast.isEmpty()) {
            subUrl  = url.substringBeforeLast("/")
        }
        val address = subUrl.substringBeforeLast("/").toLowerCase()
        var username = subUrl.substringAfterLast("/").toLowerCase()
        if (username == address) username = ""

        fun validAddress(address: String) : Boolean =  listOf(
                "https://www.github.com",
                "https://github.com",
                "www.github.com",
                "github.com"
        ).any { it == address }

        fun validUserName(username: String) : Boolean {
            val excludePath = listOf(
                    "",
                    "enterprise",
                    "features",
                    "topics",
                    "collections",
                    "trending",
                    "events",
                    "marketplace",
                    "pricing",
                    "nonprofit",
                    "customer-stories",
                    "security",
                    "login",
                    "join")

            //return !(excludePath.any{ it == username} || username.contains(Regex("^[\\W]|[^a-zA-Z0-9-]|[^a-zA-Z0-9\\/]+\$")))
            return !(excludePath.any{ it == username} || username.contains(Regex("^[\\W]|[^a-zA-Z0-9-]|(\\W{2,})|[^a-zA-Z0-9\\/]+\$")))
        }

        return url == "" || (validAddress(address) && validUserName(username))
    }


    fun Int.toBytesArray(): Array<Int>{
        val bytes = arrayOf(0,0,0,0)
        bytes[0] = this and 0xFF
        bytes[1] = (this shr 8) and 0xFF
        bytes[2] = (this shr 16) and 0xFF
        bytes[3] = (this shr 24) and 0xFF

        return bytes
    }

    fun interpolateColor(width: Int, dX: Float, initialColor: Int, finishColor: Int): Int {
        val initialColorBytes = initialColor.toBytesArray()
        val initialOpaque = initialColorBytes[3]
        val initialRed = initialColorBytes[2]
        val initialGreen = initialColorBytes[1]
        val initialBlue = initialColorBytes[0]

        val finishColorBytes = finishColor.toBytesArray()
        val finishOpaque = finishColorBytes[3]
        val finishRed = finishColorBytes[2]
        val finishGreen = finishColorBytes[1]
        val finishBlue = finishColorBytes[0]

        val currentOpaque = initialOpaque + (finishOpaque - initialOpaque)*(dX.absoluteValue/width.toFloat()).toInt()
        val currentRed = initialRed + (finishRed - initialRed)*(dX.absoluteValue/width.toFloat()).toInt()
        val currentGreen = initialGreen + (finishGreen - initialGreen)*(dX.absoluteValue/width.toFloat()).toInt()
        val currentBlue = initialBlue + (finishBlue - initialBlue)*(dX.absoluteValue/width.toFloat()).toInt()

        val currentColor = currentBlue or
                ((currentGreen shl 8) and 0x0000FF00) or
                ((currentRed shl 16) and 0x00FF0000) or
                ((currentOpaque shl 24).toLong() and 0xFF000000L).toInt()

        return currentColor
    }


    class TextDrawable constructor(builder: Builder) : ShapeDrawable(builder.shape) {

        val text: String?
        val color: Int
        val height: Int
        val width: Int

        private val textPaint: Paint
        private val borderPaint: Paint
        private val shape: RectShape?
        private val fontSize: Int
        private val radius: Float
        private val borderThickness: Int

        init {

            // shape properties
            shape = builder.shape
            height = builder.height
            width = builder.width
            radius = builder.radius

            // text and color
            text = if (builder.toUpperCase) builder.text!!.toUpperCase() else builder.text
            color = builder.color

            // text paint settings
            fontSize = builder.fontSize
            textPaint = Paint()
            textPaint.color = builder.textColor
            textPaint.isAntiAlias = true
            textPaint.isFakeBoldText = builder.isBold
            textPaint.style = Paint.Style.FILL
            textPaint.typeface = builder.font
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.strokeWidth = builder.borderThickness.toFloat()

            // border paint settings
            borderThickness = builder.borderThickness
            borderPaint = Paint()
            borderPaint.color = color
            borderPaint.style = Paint.Style.FILL_AND_STROKE
            borderPaint.strokeWidth = borderThickness.toFloat()

            // drawable paint color
            val paint = paint
            paint.color = color

        }

        private fun getDarkerShade(color: Int): Int {
            /*return Color.rgb(
                (SHADE_FACTOR * Color.red(color)).toInt(),
                (SHADE_FACTOR * Color.green(color)).toInt(),
                (SHADE_FACTOR * Color.blue(color)).toInt()
            )*/

            return Color.rgb(255,0,0)
        }

        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            val r = bounds


            // draw border
            if (borderThickness > 0) {
                drawBorder(canvas)
            }

            val count = canvas.save()
            canvas.translate(r.left.toFloat(), r.top.toFloat())

            // draw text
            val width = if (this.width < 0) r.width() else this.width
            val height = if (this.height < 0) r.height() else this.height
            val fontSize = if (this.fontSize < 0) Math.min(width, height) / 2 else this.fontSize
            textPaint.textSize = fontSize.toFloat()
            canvas.drawText(
                text!!,
                (width / 2).toFloat(),
                height / 2 - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint
            )
            canvas.restoreToCount(count)
        }

        private fun drawBorder(canvas: Canvas) {
            val rect = RectF(bounds)
            rect.inset((borderThickness / 2).toFloat(), (borderThickness / 2).toFloat())

            if (shape is OvalShape) {
                canvas.drawOval(rect, borderPaint)
            } else if (shape is RoundRectShape) {
                canvas.drawRoundRect(rect, radius, radius, borderPaint)
            } else {
                canvas.drawRect(rect, borderPaint)
            }
        }

        override fun setAlpha(alpha: Int) {
            textPaint.alpha = alpha
        }

        override fun setColorFilter(cf: ColorFilter?) {
            textPaint.colorFilter = cf
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }

        override fun getIntrinsicWidth(): Int {
            return width
        }

        override fun getIntrinsicHeight(): Int {
            return height
        }

        class Builder constructor() : IConfigBuilder, IShapeBuilder, IBuilder {

            var text: String? = null

            var color: Int = 0

            var borderThickness: Int = 0

            var width: Int = 0

            var height: Int = 0

            var font: Typeface? = null

            var shape: RectShape? = null

            var textColor: Int = 0

            var fontSize: Int = 0

            var isBold: Boolean = false

            var toUpperCase: Boolean = false

            var radius: Float = 0.toFloat()

            init {
                text = ""
                color = Color.GRAY
                textColor = Color.WHITE
                borderThickness = 0
                width = -1
                height = -1
                shape = RectShape()
                font = Typeface.create("sans-serif-light", Typeface.NORMAL)
                fontSize = -1
                isBold = false
                toUpperCase = false
            }

            override fun width(width: Int): IConfigBuilder {
                this.width = width
                return this
            }

            override fun height(height: Int): IConfigBuilder {
                this.height = height
                return this
            }

            override fun textColor(color: Int): IConfigBuilder {
                this.textColor = color
                return this
            }

            override fun withBorder(thickness: Int): IConfigBuilder {
                this.borderThickness = thickness
                return this
            }

            override fun useFont(font: Typeface): IConfigBuilder {
                this.font = font
                return this
            }

            override fun fontSize(size: Int): IConfigBuilder {
                this.fontSize = size
                return this
            }

            override fun bold(): IConfigBuilder {
                this.isBold = true
                return this
            }

            override fun toUpperCase(): IConfigBuilder {
                this.toUpperCase = true
                return this
            }

            override fun beginConfig(): IConfigBuilder {
                return this
            }

            override fun endConfig(): IShapeBuilder {
                return this
            }

            override fun rect(): IBuilder {
                this.shape = RectShape()
                return this
            }

            override fun round(): IBuilder {
                this.shape = OvalShape()
                return this
            }

            override fun roundRect(radius: Int): IBuilder {
                this.radius = radius.toFloat()
                val radii = floatArrayOf(
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat()
                )
                this.shape = RoundRectShape(radii, null, null)
                return this
            }

            override fun buildRect(text: String, color: Int): TextDrawable {
                rect()
                return build(text, color)
            }

            override fun buildRoundRect(text: String, color: Int, radius: Int): TextDrawable {
                roundRect(radius)
                return build(text, color)
            }

            override fun buildRound(text: String, color: Int): TextDrawable {
                round()
                return build(text, color)
            }

            override fun build(text: String, color: Int): TextDrawable {
                this.color = color
                this.text = text
                return TextDrawable(this)
            }
        }

        interface IConfigBuilder {
            fun width(width: Int): IConfigBuilder

            fun height(height: Int): IConfigBuilder

            fun textColor(color: Int): IConfigBuilder

            fun withBorder(thickness: Int): IConfigBuilder

            fun useFont(font: Typeface): IConfigBuilder

            fun fontSize(size: Int): IConfigBuilder

            fun bold(): IConfigBuilder

            fun toUpperCase(): IConfigBuilder

            fun endConfig(): IShapeBuilder
        }

        interface IBuilder {

            fun build(text: String, color: Int): TextDrawable
        }

        interface IShapeBuilder {

            fun beginConfig(): IConfigBuilder

            fun rect(): IBuilder

            fun round(): IBuilder

            fun roundRect(radius: Int): IBuilder

            fun buildRect(text: String, color: Int): TextDrawable

            fun buildRoundRect(text: String, color: Int, radius: Int): TextDrawable

            fun buildRound(text: String, color: Int): TextDrawable
        }

        companion object {
            private const val SHADE_FACTOR = 0.9f

            fun builder(): IShapeBuilder {
                return Builder()
            }
        }
    }

    object ColorHash {

        private val S = floatArrayOf(0.35f, 0.5f, 0.65f)
        private val L = floatArrayOf(0.35f, 0.5f, 0.65f)

        private fun hslFromStr(str: String): FloatArray {
            var hash = BKDRHash(str)

            val H =  (hash % 359).toFloat()
            hash = hash / 360
            val S = this.S[hash % this.S.size]
            hash = (hash / this.S.size)
            val L = this.L[hash % this.L.size]

            return floatArrayOf(H,S,L )
        }

        private fun BKDRHash(str: String): Int {
            val seed = 131
            val seed2 = 137
            var hash = 0
            // make hash more sensitive for short string like 'a', 'b', 'c'
            val ourString = str + 'x'
            // Note: Number.MAX_SAFE_INTEGER equals 9007199254740991
            val max_safe_int = 9007199254740991 / seed2
            for( i in 0 until ourString.length) {
                if(hash > max_safe_int) {
                    hash = (hash / seed2)
                }
                hash = hash * seed + ourString[i].toInt()
            }
            return hash
        }

        fun getColor(initials: String): Int {
            return Color.HSVToColor(hslFromStr(initials))
        }
    }



}