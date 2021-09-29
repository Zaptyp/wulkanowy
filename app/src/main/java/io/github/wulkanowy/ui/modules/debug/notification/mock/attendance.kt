package io.github.wulkanowy.ui.modules.debug.notification.mock

import io.github.wulkanowy.data.db.entities.Attendance
import java.time.LocalDate

val debugAttendanceItems = listOf(
    generateAttendance("Matematyka", "PRESENCE"),
    generateAttendance("Język angielski", "LATENESS"),
    generateAttendance("Geografia", "ABSENCE"),
    generateAttendance("Sieci komputerowe", "ABSENCE"),
    generateAttendance("Systemy operacyjne", "LATENESS"),
    generateAttendance("Język niemiecki", "ABSENCE"),
    generateAttendance("Biologia", "ABSENCE"),
    generateAttendance("Chemia", "ABSENCE"),
    generateAttendance("Fizyka", "ABSENCE"),
    generateAttendance("Matematyka", "ABSENCE"),
)

private fun generateAttendance(subject: String, name: String) = Attendance(
    subject = subject,
    studentId = 0,
    diaryId = 0,
    date = LocalDate.now(),
    timeId = 0,
    number = 1,
    name = name,
    presence = false,
    absence = false,
    exemption = false,
    lateness = false,
    excused = false,
    deleted = false,
    excusable = false,
    excuseStatus = ""
)
