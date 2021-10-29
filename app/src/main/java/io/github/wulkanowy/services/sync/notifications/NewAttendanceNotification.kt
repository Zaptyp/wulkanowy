package io.github.wulkanowy.services.sync.notifications

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.wulkanowy.R
import io.github.wulkanowy.data.db.entities.Attendance
import io.github.wulkanowy.data.db.entities.Student
import io.github.wulkanowy.data.pojos.GroupNotificationData
import io.github.wulkanowy.data.pojos.NotificationData
import io.github.wulkanowy.utils.descriptionRes
import io.github.wulkanowy.utils.getPlural
import io.github.wulkanowy.utils.toFormattedString
import javax.inject.Inject

class NewAttendanceNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager,
    @ApplicationContext private val context: Context
) {

    suspend fun notify(items: List<Attendance>, student: Student) {
        val lines = items.filterNot { it.presence || it.name == "UNKNOWN" }
            .map {
                val description = context.getString(it.descriptionRes)
                "${it.date.toFormattedString("dd.MM")} - ${it.subject}: $description"
            }
            .ifEmpty { return }

        val notificationDataList = lines.map {
            NotificationData(
                title = context.getPlural(R.plurals.attendance_notify_new_items_title, 1),
                content = it,
                intentToStart = Intent()
            )
        }

        val groupNotificationData = GroupNotificationData(
            notificationDataList = notificationDataList,
            title = context.getPlural(R.plurals.attendance_notify_new_items_title, lines.size),
            content = context.getPlural(
                R.plurals.attendance_notify_new_items,
                lines.size,
                lines.size
            ),
            intentToStart = Intent(),
            type = NotificationType.NEW_ATTENDANCE
        )

        appNotificationManager.sendMultipleNotifications(groupNotificationData, student)
    }
}
