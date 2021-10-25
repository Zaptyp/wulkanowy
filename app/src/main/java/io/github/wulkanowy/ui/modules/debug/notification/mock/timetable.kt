package io.github.wulkanowy.ui.modules.debug.notification.mock

import io.github.wulkanowy.data.db.entities.Timetable
import java.time.LocalDate
import java.time.LocalDateTime

val debugTimetableItems = listOf(
    generateTimetable("Matematyka", "12", "01"),
    generateTimetable("Język angielski", "23", "12"),
    generateTimetable("Geografia", "34", "23"),
    generateTimetable("Sieci komputerowe", "45", "34"),
    generateTimetable("Systemy operacyjne", "56", "45"),
    generateTimetable("Język niemiecki", "67", "56"),
    generateTimetable("Biologia", "78", "67"),
    generateTimetable("Chemia", "89", "78"),
    generateTimetable("Fizyka", "90", "89"),
    generateTimetable("Matematyka", "01", "90"),
)

private fun generateTimetable(subject: String, room: String, roomOld: String) = Timetable(
    subject = subject,
    studentId = 0,
    diaryId = 0,
    date = LocalDate.now(),
    number = 1,
    start = LocalDateTime.now(),
    end = LocalDateTime.now(),
    subjectOld = "",
    group = "",
    room = room,
    roomOld = roomOld,
    teacher = "Wtorkowska Renata",
    teacherOld = "",
    info = "",
    isStudentPlan = true,
    changes = true,
    canceled = true
)
