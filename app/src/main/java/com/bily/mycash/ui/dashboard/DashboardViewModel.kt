package com.bily.mycash.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bily.mycash.MyCashApp
import com.bily.mycash.data.db.entity.TransactionEntity

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MyCashApp
    private val transactionRepo = app.transactionRepository
    private val studentRepo = app.studentRepository

    val totalIncome: LiveData<Long> = transactionRepo.totalIncome
    val totalExpense: LiveData<Long> = transactionRepo.totalExpense
    val recentTransactions: LiveData<List<TransactionEntity>> = transactionRepo.getRecentTransactions(5)
    val studentCount: LiveData<Int> = studentRepo.studentCount

    // Calculated balance
    val balance: MediatorLiveData<Long> = MediatorLiveData<Long>().apply {
        var income = 0L
        var expense = 0L

        addSource(totalIncome) { inc ->
            income = inc ?: 0L
            value = income - expense
        }
        addSource(totalExpense) { exp ->
            expense = exp ?: 0L
            value = income - expense
        }
    }
}
