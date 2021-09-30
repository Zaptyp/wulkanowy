package io.github.wulkanowy.ui.modules.timetable.additional.add

import io.github.wulkanowy.ui.base.BaseView
import java.time.LocalDate

interface AdditionalLessonAddView : BaseView {

    val additionalLessonAddSuccess: String

    fun initView()

    fun checkFields()

    fun closeDialog()

    fun showDatePickerDialog(currentDate: LocalDate)

    fun showStartTimePickerDialog()

    fun showEndTimePickerDialog()
}