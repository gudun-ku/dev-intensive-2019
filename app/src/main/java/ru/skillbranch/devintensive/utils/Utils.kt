package ru.skillbranch.devintensive.utils



object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts : List<String>? = fullName?.trim()?.split(" ")
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

    private val letters = HashMap<String, String>()
    init {
        letters["А"] = "A"
        letters["Б"] = "B"
        letters["В"] = "V"
        letters["Г"] = "G"
        letters["Д"] = "D"
        letters["Е"] = "E"
        letters["Ё"] = "E"
        letters["Ж"] = "Zh"
        letters["З"] = "Z"
        letters["И"] = "I"
        letters["Й"] = "I"
        letters["К"] = "K"
        letters["Л"] = "L"
        letters["М"] = "M"
        letters["Н"] = "N"
        letters["О"] = "O"
        letters["П"] = "P"
        letters["Р"] = "R"
        letters["С"] = "S"
        letters["Т"] = "T"
        letters["У"] = "U"
        letters["Ф"] = "F"
        letters["Х"] = "Kh"
        letters["Ц"] = "C"
        letters["Ч"] = "Ch"
        letters["Ш"] = "Sh"
        letters["Щ"] = "SH'"
        letters["Ъ"] = "'"
        letters["Ы"] = "I"
        letters["Ъ"] = "'"
        letters["Э"] = "E"
        letters["Ю"] = "Yu"
        letters["Я"] = "Ya"
        letters["а"] = "a"
        letters["б"] = "b"
        letters["в"] = "v"
        letters["г"] = "g"
        letters["д"] = "d"
        letters["е"] = "e"
        letters["ё"] = "e"
        letters["ж"] = "zh"
        letters["з"] = "z"
        letters["и"] = "i"
        letters["й"] = "i"
        letters["к"] = "k"
        letters["л"] = "l"
        letters["м"] = "m"
        letters["н"] = "n"
        letters["о"] = "o"
        letters["п"] = "p"
        letters["р"] = "r"
        letters["с"] = "s"
        letters["т"] = "t"
        letters["у"] = "u"
        letters["ф"] = "f"
        letters["х"] = "h"
        letters["ц"] = "c"
        letters["ч"] = "ch"
        letters["ш"] = "sh"
        letters["щ"] = "sh'"
        letters["ъ"] = "'"
        letters["ы"] = "i"
        letters["ъ"] = "'"
        letters["э"] = "e"
        letters["ю"] = "yu"
        letters["я"] = "ya"
    }
    
    fun transliteration(payload: String?, divider: String = ""): String? {
        return when (payload){
            null -> null
            else ->  {
                val sb = StringBuilder(payload.length)
                for (i in 0 until payload.length) {
                    val l = payload.substring(i, i + 1)
                    if (l == " " && !divider.isNullOrEmpty())
                        sb.append(divider)
                    else {
                        if (letters.containsKey(l)) {
                            sb.append(letters[l])
                        } else {
                            sb.append(l)
                        }
                    }
                }
                sb.toString()
            }
        }
    }
}