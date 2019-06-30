package ru.skillbranch.devintensive.models

import java.util.*

class TextMessage(
    override val id: String,
    override val from: User?,
    override val chat: Chat,
    override val isIncoming: Boolean = false,
    override val date: Date = Date(),
    var text: String?
): BaseMessage(id, from, chat, isIncoming, date) {
    override fun formatMessage(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}