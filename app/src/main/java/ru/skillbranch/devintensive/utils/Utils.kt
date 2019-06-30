package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts : List<String>? = fullName?.split(" ")
        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)
        firstName = if (firstName.isNullOrBlank()) null else firstName
        lastName = if (lastName.isNullOrBlank()) null else lastName

        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return firstName?.getOrNull(0)?.toUpperCase().toString() + lastName?.getOrNull(0)?.toUpperCase().toString()
    }
}