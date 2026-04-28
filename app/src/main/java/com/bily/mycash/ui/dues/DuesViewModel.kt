package com.bily.mycash.ui.dues

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bily.mycash.MyCashApp
import com.bily.mycash.adapter.DueWithStudent
import com.bily.mycash.data.db.entity.DueEntity
import com.bily.mycash.data.db.entity.StudentEntity
import kotlinx.coroutines.launch

class DuesViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MyCashApp
    private val dueRepo = app.dueRepository
    private val studentRepo = app.studentRepository

    val unpaidDues: LiveData<List<DueEntity>> = dueRepo.unpaidDues
    val paidDues: LiveData<List<DueEntity>> = dueRepo.paidDues
    val unpaidCount: LiveData<Int> = dueRepo.unpaidCount

    private val _unpaidWithStudents = MutableLiveData<List<DueWithStudent>>()
    val unpaidWithStudents: LiveData<List<DueWithStudent>> = _unpaidWithStudents

    private val _paidWithStudents = MutableLiveData<List<DueWithStudent>>()
    val paidWithStudents: LiveData<List<DueWithStudent>> = _paidWithStudents

    fun loadUnpaidWithStudents(dues: List<DueEntity>) {
        viewModelScope.launch {
            val result = dues.map { due ->
                val student = studentRepo.getById(due.studentId)
                DueWithStudent(due, student)
            }
            _unpaidWithStudents.postValue(result)
        }
    }

    fun loadPaidWithStudents(dues: List<DueEntity>) {
        viewModelScope.launch {
            val result = dues.map { due ->
                val student = studentRepo.getById(due.studentId)
                DueWithStudent(due, student)
            }
            _paidWithStudents.postValue(result)
        }
    }

    fun markAsPaid(dueWithStudent: DueWithStudent) {
        viewModelScope.launch {
            val studentName = dueWithStudent.student?.name ?: "Siswa"
            dueRepo.markAsPaid(dueWithStudent.due, studentName)
        }
    }

    fun insertDue(due: DueEntity) {
        viewModelScope.launch {
            dueRepo.insert(due)
        }
    }

    suspend fun getAllStudents(): List<StudentEntity> {
        return studentRepo.getAllStudentsList()
    }
}
