package io.github.wulkanowy.data.repositories.message

import io.github.wulkanowy.data.db.dao.MessagesDao
import io.github.wulkanowy.data.db.entities.Message
import io.github.wulkanowy.data.db.entities.Student
import io.github.wulkanowy.data.repositories.message.MessageFolder.TRASHED
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageLocal @Inject constructor(private val messagesDb: MessagesDao) {

    fun saveMessages(messages: List<Message>) {
        messagesDb.insertAll(messages)
    }

    fun updateMessages(messages: List<Message>) {
        messagesDb.updateAll(messages)
    }

    fun deleteMessages(messages: List<Message>) {
        messagesDb.deleteAll(messages)
    }

    fun getMessage(student: Student, id: Int): Maybe<Message> {
        return messagesDb.load(student.studentId, id)
    }

    fun getMessages(student: Student, folder: MessageFolder): Maybe<List<Message>> {
        return when (folder) {
            TRASHED -> messagesDb.loadDeleted(student.studentId)
            else -> messagesDb.loadAll(student.studentId, folder.id)
        }.filter { it.isNotEmpty() }
    }
}
