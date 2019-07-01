package ru.skillbranch.devintensive.models

import java.util.*

class UserBuilder {

    companion object {
        private var lastId = -1

        private var id: String = "${lastId++}"
        private var firstName: String? = null
        private var lastName: String? = null
        private var avatar: String? = null
        private var rating: Int = 0
        private var respect: Int = 0
        private var lastVisit: Date? = Date()
        private var isOnline: Boolean = false
    }

    fun id(value: String) = apply { id = value }
    fun firstName(value: String?) = apply { firstName = value }
    fun lastName(value: String?) = apply { lastName = value }
    fun avatar(value: String?) = apply { avatar = value}
    fun rating(value: Int) = apply { rating = value }
    fun respect(value: Int) = apply { respect = value }
    fun lastVisit(value: Date?) = apply { lastVisit = value }
    fun isOnline(value: Boolean) = apply { isOnline = value }

    fun build(): User {
        var user = User(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
        return user
    }
}