package com.bily.mycash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bily.mycash.data.db.entity.DueEntity
import com.bily.mycash.data.db.entity.StudentEntity
import com.bily.mycash.databinding.ItemDueBinding
import com.bily.mycash.util.CurrencyFormatter
import com.bily.mycash.util.DateHelper

data class DueWithStudent(
    val due: DueEntity,
    val student: StudentEntity?
)

class DueAdapter(
    private val onPayClick: ((DueWithStudent) -> Unit)? = null
) : ListAdapter<DueWithStudent, DueAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(
        private val binding: ItemDueBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DueWithStudent) {
            val studentName = item.student?.name ?: "Siswa"
            binding.tvStudentName.text = studentName
            binding.tvInitial.text = studentName.firstOrNull()?.uppercase() ?: "?"
            binding.tvDueDate.text = "Jatuh tempo: ${DateHelper.formatDate(item.due.dueDate)}"
            binding.tvAmount.text = CurrencyFormatter.format(item.due.amount)

            if (item.due.status == "paid") {
                binding.btnPay.text = "Lunas"
                binding.btnPay.isEnabled = false
                binding.btnPay.alpha = 0.5f
            } else {
                binding.btnPay.text = "Bayar"
                binding.btnPay.isEnabled = true
                binding.btnPay.alpha = 1f
                binding.btnPay.setOnClickListener {
                    onPayClick?.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DueWithStudent>() {
        override fun areItemsTheSame(oldItem: DueWithStudent, newItem: DueWithStudent) =
            oldItem.due.id == newItem.due.id

        override fun areContentsTheSame(oldItem: DueWithStudent, newItem: DueWithStudent) =
            oldItem == newItem
    }
}
