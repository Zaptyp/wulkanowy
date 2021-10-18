package io.github.wulkanowy.services.sync.notifications

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.wulkanowy.R
import io.github.wulkanowy.data.db.entities.Student
import io.github.wulkanowy.data.db.entities.Timetable
import io.github.wulkanowy.data.pojos.MultipleNotificationsData
import io.github.wulkanowy.ui.modules.main.MainView
import io.github.wulkanowy.utils.toFormattedString
import java.time.LocalDate
import javax.inject.Inject

class ChangeTimetableNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager,
    @ApplicationContext private val context: Context,
) {

    suspend fun notify(items: List<Timetable>, student: Student) {
        val today = LocalDate.now()
        val lines = items.filter {
            !it.date.isBefore(today) && (it.roomOld.isNotBlank() || it.subjectOld.isNotBlank() || it.teacherOld.isNotBlank() || it.info.isNotBlank())
        }.map {
            var text = context.getString(
                R.string.timetable_notify_lesson,
                it.date.toFormattedString("EEE dd.MM"),
                it.number,
                it.subject
            )

            if (it.roomOld.isNotBlank()) {
                text += context.getString(
                    R.string.timetable_notify_change_room,
                    it.roomOld,
                    it.room
                )
            }
            if (it.teacherOld.isNotBlank() && it.teacher != it.teacherOld) {
                text += context.getString(
                    R.string.timetable_notify_change_teacher,
                    it.teacherOld,
                    it.teacher
                )
            }
            if (it.subjectOld.isNotBlank()) {
                text += context.getString(
                    R.string.timetable_notify_change_subject,
                    it.subjectOld,
                    it.subject
                )
            }

            text += if (it.info.isNotBlank()) "\n${it.info}" else ""
            text
        }.ifEmpty { return }

        val notification = MultipleNotificationsData(
            type = NotificationType.CHANGE_TIMETABLE,
            icon = R.drawable.ic_main_timetable,
            titleStringRes = R.plurals.timetable_notify_new_items_title,
            contentStringRes = R.plurals.timetable_notify_new_items,
            summaryStringRes = R.plurals.timetable_number_item,
            startMenu = MainView.Section.TIMETABLE,
            lines = lines
        )

        appNotificationManager.sendNotification(notification, student)
    }
}
