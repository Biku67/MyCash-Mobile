package com.bily.mycash.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bily.mycash.data.db.entity.DueEntity

@Dao
interface DueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(due: DueEntity): Long

    @Update
    suspend fun update(due: DueEntity)

    @Delete
    suspend fun delete(due: DueEntity)

    @Query("SELECT * FROM dues WHERE status = 'unpaid' ORDER BY dueDate ASC")
    fun getUnpaidDues(): LiveData<List<DueEntity>>

    @Query("SELECT * FROM dues WHERE status = 'paid' ORDER BY paidAt DESC")
    fun getPaidDues(): LiveData<List<DueEntity>>

    @Query("SELECT * FROM dues ORDER BY createdAt DESC")
    fun getAllDues(): LiveData<List<DueEntity>>

    @Query("SELECT * FROM dues WHERE id = :id")
    suspend fun getById(id: Long): DueEntity?

    @Query("SELECT * FROM dues WHERE studentId = :studentId ORDER BY dueDate DESC")
    fun getDuesByStudent(studentId: Long): LiveData<List<DueEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM dues WHERE status = 'unpaid'")
    fun getTotalUnpaid(): LiveData<Long>

    @Query("SELECT COUNT(*) FROM dues WHERE status = 'unpaid'")
    fun getUnpaidCount(): LiveData<Int>

    @Query("""
        UPDATE dues SET status = 'paid', paidAt = :paidAt WHERE id = :dueId
    """)
    suspend fun markAsPaid(dueId: Long, paidAt: Long = System.currentTimeMillis())
}
