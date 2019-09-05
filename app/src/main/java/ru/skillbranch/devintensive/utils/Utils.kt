package ru.skillbranch.devintensive.utils

import android.content.res.Resources
import ru.skillbranch.devintensive.extensions.toBytesArray
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

}