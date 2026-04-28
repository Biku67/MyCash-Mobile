package com.bily.mycash.ui.report

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
import com.bily.mycash.databinding.FragmentReportBinding
import com.bily.mycash.util.CurrencyFormatter
import com.bily.mycash.util.DateHelper
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter

    private var startDate = DateHelper.monthsAgo(6)
    private var endDate = DateHelper.endOfMonth()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransactionAdapter()
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Set initial dates
        binding.etStartDate.setText(DateHelper.formatDate(startDate))
        binding.etEndDate.setText(DateHelper.formatDate(endDate))

        // Date pickers
        binding.etStartDate.setOnClickListener {
            showDatePicker("Dari Tanggal", startDate) { millis ->
                startDate = millis
                binding.etStartDate.setText(DateHelper.formatDate(millis))
                loadData()
            }
        }

        binding.etEndDate.setOnClickListener {
            showDatePicker("Sampai Tanggal", endDate) { millis ->
                endDate = millis
                binding.etEndDate.setText(DateHelper.formatDate(millis))
                loadData()
            }
        }

        loadData()
    }

    private fun loadData() {
        // Summary
        viewModel.getIncome(startDate, endDate).observe(viewLifecycleOwner) { income ->
            binding.tvTotalIncome.text = CurrencyFormatter.format(income ?: 0)
            updateBalance()
        }

        viewModel.getExpense(startDate, endDate).observe(viewLifecycleOwner) { expense ->
            binding.tvTotalExpense.text = CurrencyFormatter.format(expense ?: 0)
            updateBalance()
        }

        // Transaction list
        viewModel.getTransactions(startDate, endDate).observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
            updateChart(transactions)
        }
    }

    private fun updateBalance() {
        val incomeText = binding.tvTotalIncome.text.toString()
            .replace("Rp ", "").replace(".", "")
        val expenseText = binding.tvTotalExpense.text.toString()
            .replace("Rp ", "").replace(".", "")

        val income = incomeText.toLongOrNull() ?: 0L
        val expense = expenseText.toLongOrNull() ?: 0L
        binding.tvNetBalance.text = CurrencyFormatter.format(income - expense)
    }

    private fun updateChart(transactions: List<com.bily.mycash.data.db.entity.TransactionEntity>) {
        val incomeColor = ContextCompat.getColor(requireContext(), R.color.color_income)
        val expenseColor = ContextCompat.getColor(requireContext(), R.color.color_expense)
        val textColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)

        // Group by month
        val monthlyIncome = mutableMapOf<String, Float>()
        val monthlyExpense = mutableMapOf<String, Float>()

        transactions.forEach { tx ->
            val monthKey = DateHelper.formatMonthYear(tx.transactionDate)
            if (tx.type == "income") {
                monthlyIncome[monthKey] = (monthlyIncome[monthKey] ?: 0f) + tx.amount.toFloat()
            } else {
                monthlyExpense[monthKey] = (monthlyExpense[monthKey] ?: 0f) + tx.amount.toFloat()
            }
        }

        val months = (monthlyIncome.keys + monthlyExpense.keys).distinct().sorted()
        if (months.isEmpty()) return

        val incomeEntries = months.mapIndexed { i, m -> BarEntry(i.toFloat(), monthlyIncome[m] ?: 0f) }
        val expenseEntries = months.mapIndexed { i, m -> BarEntry(i.toFloat(), monthlyExpense[m] ?: 0f) }

        val incomeDataSet = BarDataSet(incomeEntries, "Pemasukan").apply {
            color = incomeColor; setDrawValues(false)
        }
        val expenseDataSet = BarDataSet(expenseEntries, "Pengeluaran").apply {
            color = expenseColor; setDrawValues(false)
        }

        val barData = BarData(incomeDataSet, expenseDataSet).apply { barWidth = 0.35f }

        binding.barChart.apply {
            data = barData
            description.isEnabled = false
            setFitBars(true)
            setTouchEnabled(false)
            legend.textColor = textColor

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(months)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                this.textColor = textColor
                granularity = 1f
                labelRotationAngle = -45f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = Color.parseColor("#1AFFFFFF")
                this.textColor = textColor
                axisMinimum = 0f
            }
            axisRight.isEnabled = false

            if (months.size > 1) groupBars(0f, 0.2f, 0.05f)
            invalidate()
        }
    }

    private fun showDatePicker(title: String, current: Long, onSelected: (Long) -> Unit) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(current)
            .build()

        picker.addOnPositiveButtonClickListener { millis ->
            onSelected(millis)
        }

        picker.show(childFragmentManager, "date_picker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
