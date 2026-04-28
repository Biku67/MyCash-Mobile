package com.bily.mycash.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bily.mycash.MyCashApp
import com.bily.mycash.data.db.entity.TransactionEntity
import com.bily.mycash.util.DateHelper

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MyCashApp
    private val repo = app.transactionRepository

    private var _startDate = MutableLiveData(DateHelper.monthsAgo(6))
    private var _endDate = MutableLiveData(DateHelper.endOfMonth())

    val startDate: LiveData<Long> = _startDate
    val endDate: LiveData<Long> = _endDate

    fun setDateRange(start: Long, end: Long) {
        _startDate.value = start
        _endDate.value = end
    }

    fun getTransactions(start: Long, end: Long): LiveData<List<TransactionEntity>> {
        return repo.getTransactionsByPeriod(start, end)
    }

    fun getIncome(start: Long, end: Long): LiveData<Long> {
        return repo.getIncomeByPeriod(start, end)
    }

    fun getExpense(start: Long, end: Long): LiveData<Long> {
        return repo.getExpenseByPeriod(start, end)
    }
}
