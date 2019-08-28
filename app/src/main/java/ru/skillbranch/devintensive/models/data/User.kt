package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User (
    val id : String,
    var firstName : String?,
    var lastName : String?,
    var avatar : String?,
    var rating : Int = 0,
    var respect : Int = 0,
    var lastVisit : Date? = null,
    var isOnline : Boolean = false
) {

    constructor(id: String, firstName: String?, lastName: String?) : this (
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    companion object Factory {

        var lastId: Int = -1
        private set

        fun makeUser (fullName: String?): User {
            lastId++

            val(firstName, lastName) = Utils.parseFullName(fullName)
            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }
    }

    // builder pattern implementation

    private constructor(builder: Builder) :
            this(builder.id, builder.firstName, builder.lastName, builder.avatar, builder.rating, builder.respect, builder.lastVisit, builder.isOnline)

    class Builder {

        var id: String = "${lastId++}"
            private set
        var firstName: String? = null
            private set
        var lastName: String? = null
            private set
        var avatar: String? = null
            private set
        var rating: Int = 0
            private set
        var respect: Int = 0
            private set
        var lastVisit: Date? = Date()
            private set
        var isOnline: Boolean = false


        fun id(value: String) = apply { this.id = value }
        fun firstName(value: String?) = apply { this.firstName = value }
        fun lastName(value: String?) = apply { this.lastName = value }
        fun avatar(value: String?) = apply { this.avatar = value}
        fun rating(value: Int) = apply { this.rating = value }
        fun respect(value: Int) = apply { this.respect = value }
        fun lastVisit(value: Date?) = apply { this.lastVisit = value }
        fun isOnline(value: Boolean) = apply { this.isOnline = value }


        fun build() = User(this)
    }
}