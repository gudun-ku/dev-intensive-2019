package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class ImageMessage(
    override val id: String,
    override val from: User?,
    override val chat: Chat,
    override val isIncoming: Boolean = false,
    override val date: Date = Date(),
    var image: String?
): BaseMessage(id, from, chat, isIncoming, date) {
    override fun formatMessage(): String =
            "${from?.firstName} " +
                    "${if(isIncoming) "получил" else "отправил"} изображение \"$image\" ${date.humanizeDiff()}"
}