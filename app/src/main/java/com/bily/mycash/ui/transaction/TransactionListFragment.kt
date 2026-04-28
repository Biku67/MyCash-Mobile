package com.bily.mycash.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bily.mycash.adapter.TransactionAdapter
import com.bily.mycash.databinding.FragmentTransactionListBinding

class TransactionListFragment : Fragment() {

    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupSearch()

        binding.fabAddTransaction.setOnClickListener {
            AddTransactionSheet().show(childFragmentManager, "add_transaction")
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter()
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
    }

    private fun observeData() {
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
            binding.emptyState.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
            binding.swipeRefresh.visibility = if (transactions.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun setupSearch() {
        binding.btnSearch.setOnClickListener {
            val isVisible = binding.searchCard.visibility == View.VISIBLE
            binding.searchCard.visibility = if (isVisible) View.GONE else View.VISIBLE
            if (isVisible) {
                binding.etSearch.setText("")
                // Reset to show all
                viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
                    adapter.submitList(transactions)
                }
            }
        }

        binding.etSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.trim() ?: ""
            if (query.isNotEmpty()) {
                viewModel.searchTransactions(query).observe(viewLifecycleOwner) { results ->
                    adapter.submitList(results)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
