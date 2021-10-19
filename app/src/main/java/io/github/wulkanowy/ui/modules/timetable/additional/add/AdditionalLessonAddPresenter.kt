package io.github.wulkanowy.ui.modules.timetable.additional.add

import io.github.wulkanowy.data.Status
import io.github.wulkanowy.data.db.entities.TimetableAdditional
import io.github.wulkanowy.data.repositories.SemesterRepository
import io.github.wulkanowy.data.repositories.StudentRepository
import io.github.wulkanowy.data.repositories.TimetableRepository
import io.github.wulkanowy.ui.base.BasePresenter
import io.github.wulkanowy.ui.base.ErrorHandler
import io.github.wulkanowy.utils.flowWithResource
import io.github.wulkanowy.utils.toLocalDate
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class AdditionalLessonAddPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val timetableRepository: TimetableRepository,
    private val semesterRepository: SemesterRepository
) : BasePresenter<AdditionalLessonAddView>(errorHandler, studentRepository) {

    override fun onAttachView(view: AdditionalLessonAddView) {
        super.onAttachView(view)
        view.initView()
        Timber.i("AdditionalLesson details view was initialized")
    }

    fun showDatePicker(date: LocalDate?) {
        view?.showDatePickerDialog(date ?: LocalDate.now())
    }

    fun showStartTimePicker() {
        view?.showStartTimePickerDialog()
    }

    fun showEndTimePicker() {
        view?.showEndTimePickerDialog()
    }

    fun onAddAdditionalClicked(start: String?, end: String?, date: String?, content: String?) {
        var isError = false

        if (start.isNullOrBlank()) {
            view?.setErrorStartRequired()
            isError = true
        }

        if (end.isNullOrBlank()) {
            view?.setErrorEndRequired()
            isError = true
        }

        if (date.isNullOrBlank()) {
            view?.setErrorDateRequired()
            isError = true
        }

        if (content.isNullOrBlank()) {
            view?.setErrorContentRequired()
            isError = true
        }

        if (!isError) {
            addAdditionalLesson(
                LocalTime.parse(start!!),
                LocalTime.parse(end),
                date!!.toLocalDate(),
                content!!
            )
        }
    }

    private fun addAdditionalLesson(
        start: LocalTime,
        end: LocalTime,
        date: LocalDate,
        subject: String
    ) {
        flowWithResource {
            val student = studentRepository.getCurrentStudent()
            val semester = semesterRepository.getCurrentSemester(student)

            timetableRepository.saveAdditional(
                TimetableAdditional(
                    studentId = student.studentId,
                    diaryId = semester.diaryId,
                    start = LocalDateTime.of(date, start),
                    end = LocalDateTime.of(date, end),
                    date = date,
                    subject = subject
                ).apply { isAddedByUser = true }
            )
        }.onEach {
            when (it.status) {
                Status.LOADING -> Timber.i("AdditionalLesson insert start")
                Status.SUCCESS -> {
                    Timber.i("AdditionalLesson insert: Success")
                    view?.run {
                        showSuccessMessage()
                        closeDialog()
                    }
                }
                Status.ERROR -> {
                    Timber.i("AdditionalLesson insert result: An exception occurred")
                    errorHandler.dispatch(it.error!!)
                }
            }
        }.launch("additional")
    }
}
