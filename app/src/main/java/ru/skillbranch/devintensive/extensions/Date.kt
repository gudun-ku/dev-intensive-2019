package ru.skillbranch.devintensive.extensions

import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val YEAR = 365 * DAY

fun Date.format(pattern: String="HH:mm:ss dd.MM.yy"): String {
    val dateFormat = java.text.SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits):Date {
    var time = this.time

    time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        TimeUnits.YEAR -> value * YEAR
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {

    val diff = Date().time - this.time
    val seconds = diff / SECOND
    val minutes = diff / MINUTE
    val hours = diff / HOUR
    val days = diff / DAY




    return "2 часа назад"
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    YEAR
}