package com.bily.mycash.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bily.mycash.MyCashApp
import com.bily.mycash.R
import com.bily.mycash.data.db.entity.StudentEntity
import com.bily.mycash.databinding.SheetAddStudentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddStudentSheet : BottomSheetDialogFragment() {

    private var _binding: SheetAddStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SheetAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val nis = binding.etNis.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }
            if (nis.isEmpty()) {
                binding.etNis.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }

            val student = StudentEntity(
                name = name,
                nis = nis,
                phone = phone
            )

            val app = requireActivity().application as MyCashApp
            CoroutineScope(Dispatchers.IO).launch {
                app.studentRepository.insert(student)
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
