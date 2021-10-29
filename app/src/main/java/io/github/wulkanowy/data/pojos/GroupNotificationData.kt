package io.github.wulkanowy.data.pojos

import android.content.Intent
import io.github.wulkanowy.services.sync.notifications.NotificationType

data class GroupNotificationData(
    val notificationDataList: List<NotificationData>,
    val title: String,
    val content: String,
    val intentToStart: Intent,
    val type: NotificationType
)