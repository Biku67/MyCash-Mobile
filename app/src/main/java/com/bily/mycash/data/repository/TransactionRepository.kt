package com.bily.mycash.data.repository

import androidx.lifecycle.LiveData
import com.bily.mycash.data.db.dao.TransactionDao
import com.bily.mycash.data.db.entity.TransactionEntity

class TransactionRepository(private val dao: TransactionDao) {

    val allTransactions: LiveData<List<TransactionEntity>> = dao.getAllTransactions()
    val totalIncome: LiveData<Long> = dao.getTotalIncome()
    val totalExpense: LiveData<Long> = dao.getTotalExpense()
    val transactionCount: LiveData<Int> = dao.getTransactionCount()

    fun getRecentTransactions(limit: Int = 5): LiveData<List<TransactionEntity>> {
        return dao.getRecentTransactions(limit)
    }

    fun getTransactionsByPeriod(startDate: Long, endDate: Long): LiveData<List<TransactionEntity>> {
        return dao.getTransactionsByPeriod(startDate, endDate)
    }

    fun getIncomeByPeriod(startDate: Long, endDate: Long): LiveData<Long> {
        return dao.getIncomeByPeriod(startDate, endDate)
    }

    fun getExpenseByPeriod(startDate: Long, endDate: Long): LiveData<Long> {
        return dao.getExpenseByPeriod(startDate, endDate)
    }

    fun searchTransactions(query: String): LiveData<List<TransactionEntity>> {
        return dao.searchTransactions(query)
    }

    suspend fun insert(transaction: TransactionEntity): Long {
        return dao.insert(transaction)
    }

    suspend fun update(transaction: TransactionEntity) {
        dao.update(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        dao.delete(transaction)
    }

    suspend fun getById(id: Long): TransactionEntity? {
        return dao.getById(id)
    }
}
