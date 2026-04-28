package com.bily.mycash.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dues",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId")]
)
data class DueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val amount: Long,
    val dueDate: Long,         // Epoch millis
    val status: String = "unpaid", // "unpaid" or "paid"
    val paidAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
