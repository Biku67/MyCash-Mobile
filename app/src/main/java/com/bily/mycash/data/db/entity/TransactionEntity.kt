package com.bily.mycash.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,          // "income" or "expense"
    val amount: Long,          // Nominal dalam Rupiah
    val description: String,
    val category: String = "", // Opsional: "iuran", "pembelian", dll
    val transactionDate: Long, // Epoch millis
    val createdAt: Long = System.currentTimeMillis()
)
