package com.bily.mycash.data.repository

import androidx.lifecycle.LiveData
import com.bily.mycash.data.db.dao.DueDao
import com.bily.mycash.data.db.dao.TransactionDao
import com.bily.mycash.data.db.entity.DueEntity
import com.bily.mycash.data.db.entity.TransactionEntity

class DueRepository(
    private val dueDao: DueDao,
    private val transactionDao: TransactionDao
) {

    val unpaidDues: LiveData<List<DueEntity>> = dueDao.getUnpaidDues()
    val paidDues: LiveData<List<DueEntity>> = dueDao.getPaidDues()
    val allDues: LiveData<List<DueEntity>> = dueDao.getAllDues()
    val totalUnpaid: LiveData<Long> = dueDao.getTotalUnpaid()
    val unpaidCount: LiveData<Int> = dueDao.getUnpaidCount()

    fun getDuesByStudent(studentId: Long): LiveData<List<DueEntity>> {
        return dueDao.getDuesByStudent(studentId)
    }

    suspend fun insert(due: DueEntity): Long {
        return dueDao.insert(due)
    }

    suspend fun delete(due: DueEntity) {
        dueDao.delete(due)
    }

    suspend fun getById(id: Long): DueEntity? {
        return dueDao.getById(id)
    }

    /**
     * Tandai iuran lunas + otomatis catat sebagai pemasukan
     */
    suspend fun markAsPaid(due: DueEntity, studentName: String) {
        val now = System.currentTimeMillis()
        dueDao.markAsPaid(due.id, now)

        // Otomatis catat transaksi pemasukan
        transactionDao.insert(
            TransactionEntity(
                type = "income",
                amount = due.amount,
                description = "Iuran: $studentName",
                category = "iuran",
                transactionDate = now
            )
        )
    }
}
