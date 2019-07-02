package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class TextMessage(
    override val id: String,
    override val from: User?,
    override val chat: Chat,
    override val isIncoming: Boolean = false,
    override val date: Date = Date(),
    var text: String?
): BaseMessage(id, from, chat, isIncoming, date) {
    override fun formatMessage(): String =
        "id:$id ${from?.firstName} " +
        "${if(isIncoming) "получил" else "отправил"} сообщение \"$text\" ${date.humanizeDiff()}"

}