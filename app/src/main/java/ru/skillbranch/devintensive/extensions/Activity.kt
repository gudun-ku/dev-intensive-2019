package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.abs
import kotlin.math.round


fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.getRootView(): View {
    return findViewById<View>(android.R.id.content)
}

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = (getRootView().height - visibleBounds.height())
    //Log.d("Activity.isKeyboardOpen", "heightDiff: $heightDiff")
    val marginOfError = round(this.convertDpToPx(40F)).toInt()
    //Log.d("Activity.isKeyboardOpen", "marginOfError: $heightDiff")
    val result = (abs(heightDiff) >= abs(marginOfError))
    //Log.d("Activity.isKeyboardOpen", "Result: ${result}")
    return result
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}

