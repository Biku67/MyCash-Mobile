package com.bily.mycash.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bily.mycash.data.db.entity.StudentEntity

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity): Long

    @Update
    suspend fun update(student: StudentEntity)

    @Delete
    suspend fun delete(student: StudentEntity)

    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents(): LiveData<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY name ASC")
    suspend fun getAllStudentsList(): List<StudentEntity>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getById(id: Long): StudentEntity?

    @Query("SELECT COUNT(*) FROM students")
    fun getStudentCount(): LiveData<Int>

    @Query("SELECT * FROM students WHERE name LIKE '%' || :query || '%' OR nis LIKE '%' || :query || '%'")
    fun searchStudents(query: String): LiveData<List<StudentEntity>>
}
