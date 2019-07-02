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

    var delta = date.time - this.time
    delta = delta
    var past = true
    if (delta < 0) {
        past = false
        delta = -delta
    }


    var interval:String = when {
        delta / SECOND <= 1 -> "только что"
        delta / SECOND <= 45 && past -> "несколько секунд назад"
        delta / SECOND <= 45 && !past -> "через несколько секунд"
        delta / SECOND in 46..75 && past -> "минуту назад"
        delta / SECOND in 46..75 && !past -> "через минуту"
        delta / SECOND in 76..120 && past -> "${minutesAsPlurals(delta/ MINUTE)} назад"
        delta / SECOND in 76..120 && !past -> "через ${minutesAsPlurals(delta/ MINUTE)}"
        delta / MINUTE in 2..45 && past -> "${minutesAsPlurals(delta/ MINUTE)} назад"
        delta / MINUTE in 2..45 && !past -> "через ${minutesAsPlurals(delta/ MINUTE)}"
        delta / MINUTE in 46..75 && past -> "час назад"
        delta / MINUTE in 46..75 && !past -> "через час"
        delta / MINUTE in 76..120 && past -> "${hoursAsPlurals(delta/ HOUR)} назад"
        delta / MINUTE in 76..120 && !past -> "через ${hoursAsPlurals(delta/ HOUR)}"
        delta / HOUR in 2..12 && past -> "${hoursAsPlurals(delta/ HOUR)} назад"
        delta / HOUR in 2..12 && !past -> "через ${hoursAsPlurals(delta/ HOUR)}"
        delta / HOUR in 13..26 && past -> "день назад"
        delta / HOUR in 13..26 && !past -> "через день"
        delta / HOUR in 27..47 && past -> "${daysAsPlurals(delta/ DAY)} назад"
        delta / HOUR in 27..47 && !past -> "через ${daysAsPlurals(delta/ DAY)}"
        delta / DAY in 2..360 && past -> "${daysAsPlurals(delta/ DAY)} назад"
        delta / DAY in 2..360 && !past -> "через ${daysAsPlurals(delta/ DAY)}"
        delta / DAY > 360 && past -> "более года назад"
        delta / DAY > 360 && !past -> "более чем через год"

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