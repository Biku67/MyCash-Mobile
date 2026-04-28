package com.bily.mycash.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bily.mycash.adapter.StudentAdapter
import com.bily.mycash.databinding.FragmentStudentListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StudentViewModel by viewModels()
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupSearch()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fabAddStudent.setOnClickListener {
            AddStudentSheet().show(childFragmentManager, "add_student")
        }
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter { student ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Hapus Siswa")
                .setMessage("Yakin ingin menghapus ${student.name}?")
                .setPositiveButton("Hapus") { _, _ ->
                    viewModel.delete(student)
                }
                .setNegativeButton("Batal", null)
                .show()
        }
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = adapter
    }

    private fun observeData() {
        viewModel.allStudents.observe(viewLifecycleOwner) { students ->
            adapter.submitList(students)
            binding.emptyState.visibility = if (students.isEmpty()) View.VISIBLE else View.GONE
            binding.rvStudents.visibility = if (students.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.studentCount.observe(viewLifecycleOwner) { count ->
            binding.tvCount.text = "$count siswa"
        }
    }

    private fun setupSearch() {
        binding.etSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.trim() ?: ""
            if (query.isNotEmpty()) {
                viewModel.searchStudents(query).observe(viewLifecycleOwner) { results ->
                    adapter.submitList(results)
                }
            } else {
                viewModel.allStudents.observe(viewLifecycleOwner) { students ->
                    adapter.submitList(students)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
