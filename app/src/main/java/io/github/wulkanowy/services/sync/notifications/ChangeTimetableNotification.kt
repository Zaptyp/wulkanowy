package io.github.wulkanowy.services.sync.notifications

import io.github.wulkanowy.R
import io.github.wulkanowy.data.db.entities.Student
import io.github.wulkanowy.data.db.entities.Timetable
import io.github.wulkanowy.data.pojos.MultipleNotificationsData
import io.github.wulkanowy.ui.modules.main.MainView
import io.github.wulkanowy.utils.toFormattedString
import java.time.LocalDate
import javax.inject.Inject

class ChangeTimetableNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager
) {

    suspend fun notify(items: List<Timetable>, student: Student) {
        val today = LocalDate.now()
        val lines =
            items.filter { !it.date.isBefore(today) && !(it.roomOld.isBlank() || it.subjectOld.isBlank() || it.teacherOld.isBlank()) }
                .map {
                    var text =
                        "${it.date.toFormattedString("EEE dd.MM")} lekcja ${it.number}. - ${it.subject}\n"
                    if (it.roomOld.isNotBlank()) text += "Zmiana sali z ${it.roomOld} na ${it.room}\n"
                    if (it.teacherOld.isNotBlank()) text += "Zmiana nauczyciela z ${it.teacherOld} na ${it.teacher}\n"
                    if (it.subjectOld.isNotBlank()) text += "Zmiana przedmiotu z ${it.subjectOld} na ${it.subject}\n"
                    text += it.info
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
