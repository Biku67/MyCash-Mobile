package com.bily.mycash.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bily.mycash.data.db.entity.SettingsEntity

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun set(setting: SettingsEntity)

    @Query("SELECT value FROM settings WHERE `key` = :key")
    suspend fun get(key: String): String?

    @Query("SELECT value FROM settings WHERE `key` = :key")
    fun observe(key: String): LiveData<String?>

    @Query("DELETE FROM settings WHERE `key` = :key")
    suspend fun remove(key: String)
}
