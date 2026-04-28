package com.bily.mycash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bily.mycash.R
import com.bily.mycash.data.db.entity.TransactionEntity
import com.bily.mycash.databinding.ItemTransactionBinding
import com.bily.mycash.util.CurrencyFormatter
import com.bily.mycash.util.DateHelper

class TransactionAdapter(
    private val onItemClick: ((TransactionEntity) -> Unit)? = null
) : ListAdapter<TransactionEntity, TransactionAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionEntity) {
            val isIncome = item.type == "income"
            val context = binding.root.context

            binding.tvDescription.text = item.description
            binding.tvDate.text = DateHelper.formatDate(item.transactionDate)
            binding.tvAmount.text = CurrencyFormatter.formatSigned(item.amount, isIncome)

            if (isIncome) {
                binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.color_income))
                binding.iconFrame.setBackgroundResource(R.drawable.bg_icon_income)
                binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_income))
            } else {
                binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.color_expense))
                binding.iconFrame.setBackgroundResource(R.drawable.bg_icon_expense)
                binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_expense))
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<TransactionEntity>() {
        override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity) =
            oldItem == newItem
    }
}
