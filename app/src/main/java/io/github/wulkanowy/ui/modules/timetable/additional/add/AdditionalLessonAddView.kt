package io.github.wulkanowy.ui.modules.timetable.additional.add

import io.github.wulkanowy.ui.base.BaseView
import java.time.LocalDate

interface AdditionalLessonAddView : BaseView {

    fun initView()

    fun closeDialog()

    fun showDatePickerDialog(currentDate: LocalDate)

    fun showStartTimePickerDialog()

    fun showEndTimePickerDialog()

    fun showSuccessMessage()

    fun setErrorDateRequired()

    fun setErrorStartRequired()

    fun setErrorEndRequired()

    fun setErrorContentRequired()
}
