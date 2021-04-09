package com.jandroidlibrary.jblockcannary.dao

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */

@Entity(tableName = "j_block")
class BlockBean {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long = 0

    var methodName: String = ""
    var date: String = ""
    var stackInfo: String = ""
    var blockTime: Long = 0L

    override fun toString(): String {
        return "卡顿日期:\n$date\n\n卡顿时间:\n$blockTime ms\n\n堆栈信息:\n$stackInfo\n----------------------------------\n"
    }


}