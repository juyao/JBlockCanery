package com.jandroidlibrary.jblockcannary.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */
@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBlock(blockBean: BlockBean): Long

    @Query("select * from j_block")
    fun queryAllBlock(): LiveData<List<BlockBean>>

    @Query("delete from j_block")
    fun deleteAllBlock()

    @Query("select * from j_block where id like :id limit 1")
    fun findBlock(id: Long): BlockBean


}