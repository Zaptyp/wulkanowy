package io.github.wulkanowy.ui.modules.timetable.additional.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
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
        with(binding) {
            additionalLessonDialogStartEdit.doOnTextChanged { _, _, _, _ ->
                additionalLessonDialogStart.isErrorEnabled = false
                additionalLessonDialogStart.error = null
            }
            additionalLessonDialogEndEdit.doOnTextChanged { _, _, _, _ ->
                additionalLessonDialogEnd.isErrorEnabled = false
                additionalLessonDialogEnd.error = null
            }
            additionalLessonDialogDateEdit.doOnTextChanged { _, _, _, _ ->
                additionalLessonDialogDate.isErrorEnabled = false
                additionalLessonDialogDate.error = null
            }
            additionalLessonDialogContentEdit.doOnTextChanged { _, _, _, _ ->
                additionalLessonDialogContent.isErrorEnabled = false
                additionalLessonDialogContent.error = null
            }

            additionalLessonDialogAdd.setOnClickListener {
                presenter.onAddAdditionalClicked(
                    start = additionalLessonDialogStartEdit.text?.toString(),
                    end = additionalLessonDialogEndEdit.text?.toString(),
                    date = additionalLessonDialogDateEdit.text?.toString(),
                    content = additionalLessonDialogContentEdit.text?.toString()
                )
            }
            additionalLessonDialogClose.setOnClickListener { dismiss() }
            additionalLessonDialogDate.editText?.setOnClickListener { presenter.showDatePicker(date) }
            additionalLessonDialogStart.editText?.setOnClickListener { presenter.showStartTimePicker() }
            additionalLessonDialogEnd.editText?.setOnClickListener { presenter.showEndTimePicker() }
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
            binding.additionalLessonDialogDateEdit.setText(date?.toFormattedString())
        }

        if (!parentFragmentManager.isStateSaved) {
            datePicker.show(parentFragmentManager, null)
        }
    }

    override fun showStartTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(15)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            start = LocalTime.of(timePicker.hour, timePicker.minute)
            binding.additionalLessonDialogStartEdit.setText(start?.toString())
        }

        if (!parentFragmentManager.isStateSaved) {
            timePicker.show(parentFragmentManager, null)
        }
    }

    override fun showEndTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(15)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            end = LocalTime.of(timePicker.hour, timePicker.minute)
            binding.additionalLessonDialogEndEdit.setText(end?.toString())
        }

        if (!parentFragmentManager.isStateSaved) {
            timePicker.show(parentFragmentManager, null)
        }
    }

    override fun onDestroyView() {
        presenter.onDetachView()
        super.onDestroyView()
    }
}
