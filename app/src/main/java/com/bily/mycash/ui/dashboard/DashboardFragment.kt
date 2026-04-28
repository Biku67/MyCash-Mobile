package com.bily.mycash.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bily.mycash.R
import com.bily.mycash.adapter.TransactionAdapter
import com.bily.mycash.databinding.FragmentDashboardBinding
import com.bily.mycash.ui.transaction.AddTransactionSheet
import com.bily.mycash.util.CurrencyFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupChart()

        binding.fabAddTransaction.setOnClickListener {
            AddTransactionSheet().show(childFragmentManager, "add_transaction")
        }

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.nav_transactions)
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter()
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecentTransactions.adapter = adapter
    }

    private fun observeData() {
        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            binding.tvBalance.text = CurrencyFormatter.format(balance ?: 0)
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvIncome.text = CurrencyFormatter.format(income ?: 0)
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.tvExpense.text = CurrencyFormatter.format(expense ?: 0)
        }

        viewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
            binding.emptyState.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
            binding.rvRecentTransactions.visibility = if (transactions.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun setupChart() {
        val chart = binding.barChart
        val incomeColor = ContextCompat.getColor(requireContext(), R.color.color_income)
        val expenseColor = ContextCompat.getColor(requireContext(), R.color.color_expense)
        val textColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)

        // Placeholder data — will be dynamic later
        val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun")
        val incomeEntries = months.indices.map { BarEntry(it.toFloat(), 0f) }
        val expenseEntries = months.indices.map { BarEntry(it.toFloat(), 0f) }

        val incomeDataSet = BarDataSet(incomeEntries, "Pemasukan").apply {
            color = incomeColor
            setDrawValues(false)
        }
        val expenseDataSet = BarDataSet(expenseEntries, "Pengeluaran").apply {
            color = expenseColor
            setDrawValues(false)
        }

        val barData = BarData(incomeDataSet, expenseDataSet).apply {
            barWidth = 0.35f
        }

        chart.apply {
            data = barData
            description.isEnabled = false
            setFitBars(true)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setTouchEnabled(false)
            legend.textColor = textColor
            legend.textSize = 10f

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(months)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                this.textColor = textColor
                granularity = 1f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = Color.parseColor("#1AFFFFFF")
                this.textColor = textColor
                axisMinimum = 0f
            }

            axisRight.isEnabled = false

            groupBars(0f, 0.2f, 0.05f)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
