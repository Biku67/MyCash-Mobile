package com.bily.mycash.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bily.mycash.data.db.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int = 5): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'income'")
    fun getTotalIncome(): LiveData<Long>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'expense'")
    fun getTotalExpense(): LiveData<Long>

    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = 'income' AND transactionDate BETWEEN :startDate AND :endDate
    """)
    fun getIncomeByPeriod(startDate: Long, endDate: Long): LiveData<Long>

    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = 'expense' AND transactionDate BETWEEN :startDate AND :endDate
    """)
    fun getExpenseByPeriod(startDate: Long, endDate: Long): LiveData<Long>

    @Query("""
        SELECT * FROM transactions 
        WHERE transactionDate BETWEEN :startDate AND :endDate 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByPeriod(startDate: Long, endDate: Long): LiveData<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE description LIKE '%' || :query || '%' 
        ORDER BY transactionDate DESC
    """)
    fun searchTransactions(query: String): LiveData<List<TransactionEntity>>

    @Query("SELECT COUNT(*) FROM transactions")
    fun getTransactionCount(): LiveData<Int>
}
