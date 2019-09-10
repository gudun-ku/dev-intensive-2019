package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        return messages.filter { !it.isReaded }.count()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        return if (messages.isEmpty()) return Date() else {
            val sortedMessages = messages.sortedBy { it.date }
            sortedMessages.last().date
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> = when(val lastMessage = messages.lastOrNull()){
       //TODO implement me
       null -> "Сообщений еще нет" to "@John_Doe"
       else -> {
           if (lastMessage is TextMessage) lastMessage.text!! to lastMessage.from!!.firstName else "Изображение" to lastMessage.from!!.firstName
       }
    }

    private fun isSingle(): Boolean = members.size == 1 && !isArchived
    private fun isGroup(): Boolean = members.size > 1 && !isArchived

    fun toChatItem(): ChatItem {
        return when {isSingle() -> {
                val user = members.first()
                ChatItem(
                    id,
                    user.avatar,
                    Utils.toInitials(user.firstName, user.lastName) ?: "??",
                    "${user.firstName ?: ""} ${user.lastName ?: ""}",
                    lastMessageShort().first,
                    unreadableMessageCount(),//messages.count(), //
                    lastMessageDate()?.shortFormat(),
                    user.isOnline,
                    ChatType.SINGLE,
                    lastMessageShort().second
                )
        }
        isGroup() ->
            {
                val user = members.first()
                ChatItem(
                    id,
                    null,
                    Utils.toInitials(user.firstName, null) ?: "??",
                    title,
                    lastMessageShort().first,
                    unreadableMessageCount(),//messages.count(), //
                    lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.GROUP,
                    lastMessageShort().second
                )
        }
        else ->
            {
                val archivedChats = ChatRepository.loadChats().value!!.filter { it.isArchived }
                .sortedBy { it.lastMessageDate() }
                ChatItem(
                    "-1",
                    null,
                    "",
                    "",
                    archivedChats.last().lastMessageShort().first,
                    archivedChats.sumBy{it.unreadableMessageCount()},//messages.count(), //
                    archivedChats.last().lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.ARCHIVE,
                    archivedChats.last().lastMessageShort()?.second
                )
            }
        }

    }

    fun toArchivedChatItem(): ChatItem {
        val user = members.first()
        return ChatItem(
            id,
            user.avatar,
            Utils.toInitials(user.firstName, user.lastName) ?: "??",
            "${user.firstName ?: ""} ${user.lastName ?: ""}",
            lastMessageShort().first,
            unreadableMessageCount(),//messages.count(), //
            lastMessageDate()?.shortFormat(),
            user.isOnline,
            ChatType.SINGLE,
            lastMessageShort().second
        )
    }

}

enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}



