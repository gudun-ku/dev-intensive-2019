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

    var delta = Date().time - this.time
    delta = delta
    var past = true
    if (delta < 0) {
        past = false
        delta = -delta
    }

    var interval:String = when {
        delta / SECOND <= 1 -> "только что"
        delta / SECOND <= 45 && past -> "меньше минуты"
        delta / SECOND <= 45 && !past -> "меньше чем через минуту"
        delta / SECOND in 46..75 && past -> "минуту"
        delta / SECOND in 46..75 && !past -> "через минуту"
        delta / SECOND in 76..120 && past -> "${minutesAsPlurals(delta/ MINUTE)} назад"
        delta / SECOND in 76..120 && !past -> "через ${minutesAsPlurals(delta/ MINUTE)}"



        else -> "никогда"
    }




    return interval
}

private fun minutesAsPlurals(value: Long) = when (value.asPlurals) {
    TimePlurals.ONE -> "$value минуту"
    TimePlurals.FEW -> "$value минуты"
    TimePlurals.MANY -> "$value минут"
}

private fun hoursAsPlurals(value: Long) = when (value.asPlurals) {
    TimePlurals.ONE -> "$value час"
    TimePlurals.FEW -> "$value часа"
    TimePlurals.MANY -> "$value часов"
}

private fun daysAsPlurals(value: Long) = when (value.asPlurals) {
    TimePlurals.ONE -> "$value день"
    TimePlurals.FEW -> "$value дня"
    TimePlurals.MANY -> "$value дней"
}

private fun yearsAsPlurals(value: Long) = when (value.asPlurals) {
    TimePlurals.ONE -> "$value год"
    TimePlurals.FEW -> "$value года"
    TimePlurals.MANY -> "$value лет"
}






val Long.asPlurals
    get() = when {
        this in 5L..20L -> TimePlurals.MANY
        this % 10L == 1L -> TimePlurals.ONE
        this % 10L in 2L..4L -> TimePlurals.FEW
        else -> TimePlurals.MANY
    }

enum class TimePlurals {
    ONE,
    FEW,
    MANY
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    YEAR
}