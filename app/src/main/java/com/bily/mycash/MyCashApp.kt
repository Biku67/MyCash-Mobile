package com.bily.mycash

import android.app.Application
import com.bily.mycash.data.db.MyCashDatabase
import com.bily.mycash.data.repository.DueRepository
import com.bily.mycash.data.repository.StudentRepository
import com.bily.mycash.data.repository.TransactionRepository

class MyCashApp : Application() {

    val database: MyCashDatabase by lazy {
        MyCashDatabase.getInstance(this)
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }

    val studentRepository: StudentRepository by lazy {
        StudentRepository(database.studentDao())
    }

    val dueRepository: DueRepository by lazy {
        DueRepository(database.dueDao(), database.transactionDao())
    }
}
