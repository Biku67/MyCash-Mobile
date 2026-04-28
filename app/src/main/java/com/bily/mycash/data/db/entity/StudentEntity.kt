package com.bily.mycash.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val nis: String,
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
