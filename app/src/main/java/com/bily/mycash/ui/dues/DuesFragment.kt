package com.bily.mycash.ui.dues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bily.mycash.adapter.DueAdapter
import com.bily.mycash.databinding.FragmentDuesBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

class DuesFragment : Fragment() {

    private var _binding: FragmentDuesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DuesViewModel by viewModels()
    private lateinit var adapter: DueAdapter
    private var currentTab = 0 // 0 = unpaid, 1 = paid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabs()
        observeData()

        binding.fabAddDue.setOnClickListener {
            CreateDueSheet().show(childFragmentManager, "create_due")
        }
    }

    private fun setupRecyclerView() {
        adapter = DueAdapter { dueWithStudent ->
            // Confirm payment
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi Pembayaran")
                .setMessage("Tandai pembayaran ${dueWithStudent.student?.name ?: "siswa"} sebagai lunas?")
                .setPositiveButton("Bayar") { _, _ ->
                    viewModel.markAsPaid(dueWithStudent)
                }
                .setNegativeButton("Batal", null)
                .show()
        }
        binding.rvDues.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDues.adapter = adapter
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                updateList()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeData() {
        viewModel.unpaidDues.observe(viewLifecycleOwner) { dues ->
            viewModel.loadUnpaidWithStudents(dues)
            updateProgress()
        }

        viewModel.paidDues.observe(viewLifecycleOwner) { dues ->
            viewModel.loadPaidWithStudents(dues)
            updateProgress()
        }

        viewModel.unpaidWithStudents.observe(viewLifecycleOwner) {
            if (currentTab == 0) updateList()
        }

        viewModel.paidWithStudents.observe(viewLifecycleOwner) {
            if (currentTab == 1) updateList()
        }
    }

    private fun updateList() {
        val list = if (currentTab == 0) {
            viewModel.unpaidWithStudents.value ?: emptyList()
        } else {
            viewModel.paidWithStudents.value ?: emptyList()
        }
        adapter.submitList(list)
        binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        binding.rvDues.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun updateProgress() {
        val unpaid = viewModel.unpaidDues.value?.size ?: 0
        val paid = viewModel.paidDues.value?.size ?: 0
        val total = unpaid + paid

        if (total > 0) {
            val percent = (paid * 100) / total
            binding.tvProgress.text = "$percent%"
            binding.progressBar.progress = percent
        } else {
            binding.tvProgress.text = "0%"
            binding.progressBar.progress = 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
