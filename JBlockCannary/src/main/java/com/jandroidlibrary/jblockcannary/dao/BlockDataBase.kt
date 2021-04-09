package com.jandroidlibrary.jblockcannary.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jandroidlibrary.jblockcannary.JBlockCanary

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */
@Database(entities = [BlockBean::class], version = 1)
abstract class BlockDataBase : RoomDatabase() {
    abstract fun blockDao(): BlockDao

    companion object {
        @Volatile
        private var INSTANCE: BlockDataBase? = null
        fun getInstance(): BlockDataBase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase().also { INSTANCE = it }
        }

        private fun buildDatabase() =
            Room.databaseBuilder(
                JBlockCanary.mContext!!,
                BlockDataBase::class.java, "jblock.db"
            )
                .allowMainThreadQueries()
                .build()
    }


}