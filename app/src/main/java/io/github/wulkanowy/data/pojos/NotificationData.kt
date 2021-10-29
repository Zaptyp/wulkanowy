package io.github.wulkanowy.data.pojos

import android.content.Intent

data class NotificationData(
    val intentToStart: Intent,
    val title: String,
    val content: String
)

