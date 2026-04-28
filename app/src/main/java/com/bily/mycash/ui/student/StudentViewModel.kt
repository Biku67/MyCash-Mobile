package com.bily.mycash.ui.student

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bily.mycash.MyCashApp
import com.bily.mycash.data.db.entity.StudentEntity
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MyCashApp
    private val repo = app.studentRepository

    val allStudents: LiveData<List<StudentEntity>> = repo.allStudents
    val studentCount: LiveData<Int> = repo.studentCount

    fun insert(student: StudentEntity) {
        viewModelScope.launch {
            repo.insert(student)
        }
    }

    fun delete(student: StudentEntity) {
        viewModelScope.launch {
            repo.delete(student)
        }
    }

    fun searchStudents(query: String): LiveData<List<StudentEntity>> {
        return repo.searchStudents(query)
    }
}
