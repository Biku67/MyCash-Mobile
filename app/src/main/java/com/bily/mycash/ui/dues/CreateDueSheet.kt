package com.bily.mycash.ui.dues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bily.mycash.MyCashApp
import com.bily.mycash.R
import com.bily.mycash.data.db.entity.DueEntity
import com.bily.mycash.data.db.entity.StudentEntity
import com.bily.mycash.util.DateHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateDueSheet : BottomSheetDialogFragment() {

    private var selectedStudentId: Long = -1
    private var selectedDate: Long = System.currentTimeMillis()
    private var students: List<StudentEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(R.color.bg_surface, null))
            setPadding(64, 48, 64, 48)
        }

        // Handle bar
        val handleBar = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(120, 12).apply {
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                bottomMargin = 48
            }
            setBackgroundResource(R.drawable.bg_handle_bar)
        }
        root.addView(handleBar)

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.due_create)
            setTextColor(resources.getColor(R.color.text_primary, null))
            textSize = 20f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 48)
        }
        root.addView(title)

        // Student dropdown
        val studentLayout = TextInputLayout(requireContext(), null, com.google.android.material.R.attr.textInputOutlinedExposedDropdownMenuStyle).apply {
            hint = getString(R.string.due_select_student)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 32 }
        }
        val studentDropdown = MaterialAutoCompleteTextView(studentLayout.context).apply {
            inputType = 0
        }
        studentLayout.addView(studentDropdown)
        root.addView(studentLayout)

        // Amount
        val amountLayout = TextInputLayout(requireContext(), null, com.google.android.material.R.attr.textInputOutlinedStyle).apply {
            hint = getString(R.string.due_amount)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 32 }
        }
        val etAmount = TextInputEditText(amountLayout.context).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        amountLayout.addView(etAmount)
        root.addView(amountLayout)

        // Due date
        val dateLayout = TextInputLayout(requireContext(), null, com.google.android.material.R.attr.textInputOutlinedStyle).apply {
            hint = getString(R.string.due_date)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 48 }
        }
        val etDate = TextInputEditText(dateLayout.context).apply {
            inputType = 0
            isFocusable = false
            setText(DateHelper.formatDate(selectedDate))
            setOnClickListener {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih Jatuh Tempo")
                    .setSelection(selectedDate)
                    .build()
                picker.addOnPositiveButtonClickListener { millis ->
                    selectedDate = millis
                    setText(DateHelper.formatDate(millis))
                }
                picker.show(childFragmentManager, "date_picker")
            }
        }
        dateLayout.addView(etDate)
        root.addView(dateLayout)

        // Save button
        val btnSave = com.google.android.material.button.MaterialButton(requireContext()).apply {
            text = getString(R.string.btn_save)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 144
            )
            setBackgroundColor(resources.getColor(R.color.accent_lime, null))
            setTextColor(resources.getColor(R.color.text_on_accent, null))
            cornerRadius = 36
        }
        root.addView(btnSave)

        // Load students
        val app = requireActivity().application as MyCashApp
        CoroutineScope(Dispatchers.IO).launch {
            students = app.studentRepository.getAllStudentsList()
            val names = students.map { "${it.name} (${it.nis})" }
            withContext(Dispatchers.Main) {
                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names)
                studentDropdown.setAdapter(arrayAdapter)
                studentDropdown.setOnItemClickListener { _, _, position, _ ->
                    selectedStudentId = students[position].id
                }
            }
        }

        // Save action
        btnSave.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            if (selectedStudentId == -1L) {
                Toast.makeText(requireContext(), "Pilih siswa terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amountStr.isEmpty()) {
                etAmount.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }

            val amount = amountStr.toLongOrNull() ?: 0L
            val due = DueEntity(
                studentId = selectedStudentId,
                amount = amount,
                dueDate = selectedDate
            )

            CoroutineScope(Dispatchers.IO).launch {
                app.dueRepository.insert(due)
            }

            Toast.makeText(requireContext(), R.string.success_saved, Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return root
    }
}
