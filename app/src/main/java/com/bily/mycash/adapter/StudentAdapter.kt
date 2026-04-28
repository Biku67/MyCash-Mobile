package com.bily.mycash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bily.mycash.data.db.entity.StudentEntity
import com.bily.mycash.databinding.ItemStudentBinding

class StudentAdapter(
    private val onDeleteClick: ((StudentEntity) -> Unit)? = null
) : ListAdapter<StudentEntity, StudentAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(
        private val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentEntity) {
            binding.tvName.text = item.name
            binding.tvNis.text = "NIS: ${item.nis}"
            binding.tvInitial.text = item.name.firstOrNull()?.uppercase() ?: "?"
            binding.tvPhone.text = if (item.phone.isNotEmpty()) item.phone else ""

            binding.btnDelete.setOnClickListener {
                onDeleteClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<StudentEntity>() {
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity) =
            oldItem == newItem
    }
}
