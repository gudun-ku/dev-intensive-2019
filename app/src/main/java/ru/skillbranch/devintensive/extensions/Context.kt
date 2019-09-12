package ru.skillbranch.devintensive.extensions

import android.content.Context
import android.util.TypedValue


fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.resources.displayMetrics
    )
}

fun Context.colorFromAttribute(attribute: Int): Int {
    val value = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attribute, value, true)
    return value.data
}

