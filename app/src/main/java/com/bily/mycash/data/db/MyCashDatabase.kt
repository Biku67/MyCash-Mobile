package com.bily.mycash.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bily.mycash.data.db.dao.DueDao
import com.bily.mycash.data.db.dao.SettingsDao
import com.bily.mycash.data.db.dao.StudentDao
import com.bily.mycash.data.db.dao.TransactionDao
import com.bily.mycash.data.db.entity.DueEntity
import com.bily.mycash.data.db.entity.SettingsEntity
import com.bily.mycash.data.db.entity.StudentEntity
import com.bily.mycash.data.db.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        StudentEntity::class,
        DueEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MyCashDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun studentDao(): StudentDao
    abstract fun dueDao(): DueDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: MyCashDatabase? = null

        fun getInstance(context: Context): MyCashDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyCashDatabase::class.java,
                    "mycash_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
