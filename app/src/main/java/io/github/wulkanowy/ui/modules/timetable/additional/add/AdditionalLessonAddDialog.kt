package io.github.wulkanowy.ui.modules.timetable.additional.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import io.github.wulkanowy.R
import io.github.wulkanowy.databinding.DialogAdditionalAddBinding
import io.github.wulkanowy.ui.base.BaseDialogFragment
import io.github.wulkanowy.utils.toFormattedString
import io.github.wulkanowy.utils.toLocalDateTime
import io.github.wulkanowy.utils.toTimestamp
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalLessonAddDialog : BaseDialogFragment<DialogAdditionalAddBinding>(),
    AdditionalLessonAddView {

    @Inject
    lateinit var presenter: AdditionalLessonAddPresenter

    private var date: LocalDate? = null
    private var start: LocalTime? = null
    private var end: LocalTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogAdditionalAddBinding.inflate(inflater).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttachView(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (presenter.isAdditionalLessonFullscreen) {
            dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        } else {
            dialog?.window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
        }

        with(binding) {
            additionalLessonDialogAdd.setOnClickListener {
                presenter.onAddAdditionalClicked(
                    start = additionalLessonDialogStart.editText?.text?.toString(),
                    end = additionalLessonDialogEnd.editText?.text?.toString(),
                    date = additionalLessonDialogDate.editText?.text?.toString(),
                    content = additionalLessonDialogContent.editText?.text?.toString()
                )
            }
            additionalLessonDialogClose.setOnClickListener { dismiss() }
            additionalLessonDialogDate.editText?.setOnClickListener { presenter.showDatePicker(date) }
            additionalLessonDialogStart.editText?.setOnClickListener { presenter.showStartTimePicker() }
            additionalLessonDialogEnd.editText?.setOnClickListener { presenter.showEndTimePicker() }
            additionalLessonDialogFullScreen.visibility =
                if (presenter.isAdditionalLessonFullscreen) View.GONE else View.VISIBLE
            additionalLessonDialogFullScreenExit.visibility =
                if (presenter.isAdditionalLessonFullscreen) View.VISIBLE else View.GONE
            additionalLessonDialogFullScreen.setOnClickListener {
                additionalLessonDialogFullScreen.visibility = View.GONE
                additionalLessonDialogFullScreenExit.visibility = View.VISIBLE
                dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
                presenter.isAdditionalLessonFullscreen = true

            }
            additionalLessonDialogFullScreenExit.setOnClickListener {
                additionalLessonDialogFullScreen.visibility = View.VISIBLE
                additionalLessonDialogFullScreenExit.visibility = View.GONE
                dialog?.window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
                presenter.isAdditionalLessonFullscreen = false

            }
        }
    }

    override fun showSuccessMessage() {
        showMessage(getString(R.string.additional_lessons_add_success))
    }

    override fun setErrorDateRequired() {
        with(binding.additionalLessonDialogDate) {
            isErrorEnabled = true
            error = getString(R.string.error_field_required)
        }
    }

    override fun setErrorStartRequired() {
        with(binding.additionalLessonDialogStart) {
            isErrorEnabled = true
            error = getString(R.string.error_field_required)
        }
    }

    override fun setErrorEndRequired() {
        with(binding.additionalLessonDialogEnd) {
            isErrorEnabled = true
            error = getString(R.string.error_field_required)
        }
    }

    override fun setErrorContentRequired() {
        with(binding.additionalLessonDialogContent) {
            isErrorEnabled = true
            error = getString(R.string.error_field_required)
        }
    }

    override fun closeDialog() {
        dismiss()
    }

    override fun showDatePickerDialog(currentDate: LocalDate) {

        val constraintsBuilder = CalendarConstraints.Builder().apply {
            setStart(LocalDate.now().toEpochDay())
        }
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(currentDate.toTimestamp())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            date = it.toLocalDateTime().toLocalDate()
            binding.additionalLessonDialogDate.editText?.setText(date!!.toFormattedString())
        }

        datePicker.show(this.parentFragmentManager, "null")
    }

    override fun showStartTimePickerDialog() {
        val isSystem24Hour = is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(15)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            start = LocalTime.of(timePicker.hour, timePicker.minute)
            binding.additionalLessonDialogStart.editText?.setText(start!!.toString())
        }

        timePicker.show(this.parentFragmentManager, "null")
    }

    override fun showEndTimePickerDialog() {
        val isSystem24Hour = is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(15)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            end = LocalTime.of(timePicker.hour, timePicker.minute)
            binding.additionalLessonDialogEnd.editText?.setText(end!!.toString())
        }

        timePicker.show(this.parentFragmentManager, "null")
    }

    override fun onDestroyView() {
        presenter.onDetachView()
        super.onDestroyView()
    }
}
