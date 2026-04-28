package com.bily.mycash.data.repository

import androidx.lifecycle.LiveData
import com.bily.mycash.data.db.dao.StudentDao
import com.bily.mycash.data.db.entity.StudentEntity

class StudentRepository(private val dao: StudentDao) {

    val allStudents: LiveData<List<StudentEntity>> = dao.getAllStudents()
    val studentCount: LiveData<Int> = dao.getStudentCount()

    suspend fun getAllStudentsList(): List<StudentEntity> {
        return dao.getAllStudentsList()
    }

    fun searchStudents(query: String): LiveData<List<StudentEntity>> {
        return dao.searchStudents(query)
    }

    suspend fun insert(student: StudentEntity): Long {
        return dao.insert(student)
    }

    suspend fun update(student: StudentEntity) {
        dao.update(student)
    }

    suspend fun delete(student: StudentEntity) {
        dao.delete(student)
    }

    suspend fun getById(id: Long): StudentEntity? {
        return dao.getById(id)
    }
}
