package com.bily.mycash.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bily.mycash.MyCashApp
import com.bily.mycash.data.db.entity.TransactionEntity
import com.bily.mycash.databinding.SheetAddTransactionBinding
import com.bily.mycash.util.DateHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.bily.mycash.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTransactionSheet : BottomSheetDialogFragment() {

    private var _binding: SheetAddTransactionBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Long = System.currentTimeMillis()
    private var selectedType: String = "income"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SheetAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set default date
        binding.etDate.setText(DateHelper.formatDate(selectedDate))

        // Type toggle
        binding.toggleType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedType = if (checkedId == R.id.btnIncome) "income" else "expense"
            }
        }

        // Date picker
        binding.etDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(selectedDate)
                .build()

            picker.addOnPositiveButtonClickListener { millis ->
                selectedDate = millis
                binding.etDate.setText(DateHelper.formatDate(millis))
            }

            picker.show(childFragmentManager, "date_picker")
        }

        // Save
        binding.btnSave.setOnClickListener {
            val amountStr = binding.etAmount.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (amountStr.isEmpty()) {
                binding.etAmount.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.etDescription.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }

            val amount = amountStr.toLongOrNull() ?: 0L
            if (amount <= 0) {
                binding.etAmount.error = "Jumlah harus lebih dari 0"
                return@setOnClickListener
            }

            val transaction = TransactionEntity(
                type = selectedType,
                amount = amount,
                description = description,
                transactionDate = selectedDate
            )

            val app = requireActivity().application as MyCashApp
            CoroutineScope(Dispatchers.IO).launch {
                app.transactionRepository.insert(transaction)
            }

            Toast.makeText(requireContext(), R.string.success_saved, Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
