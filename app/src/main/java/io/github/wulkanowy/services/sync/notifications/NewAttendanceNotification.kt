package io.github.wulkanowy.services.sync.notifications

import io.github.wulkanowy.R
import io.github.wulkanowy.data.db.entities.Attendance
import io.github.wulkanowy.data.db.entities.Student
import io.github.wulkanowy.data.pojos.MultipleNotificationsData
import io.github.wulkanowy.ui.modules.main.MainView
import io.github.wulkanowy.utils.toFormattedString
import java.time.LocalDate
import javax.inject.Inject

class NewAttendanceNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager
) {

    suspend fun notify(items: List<Attendance>, student: Student) {
        val today = LocalDate.now()
        val lines = items.filter { !it.date.isBefore(today) }.map {
            if (it.presence) return
            "${it.date.toFormattedString("dd.MM")} - ${it.subject}: ${it.name}"
        }.ifEmpty { return }

        val notification = MultipleNotificationsData(
            type = NotificationType.NEW_ATTENDANCE,
            icon = R.drawable.ic_main_attendance,
            titleStringRes = R.plurals.attendance_notify_new_items_title,
            contentStringRes = R.plurals.attendance_notify_new_items,
            summaryStringRes = R.plurals.attendance_number_item,
            startMenu = MainView.Section.ATTENDANCE,
            lines = lines
        )

        appNotificationManager.sendNotification(notification, student)
    }
}
