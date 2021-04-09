package com.jandroidlibrary.jblockcannary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jandroidlibrary.jblockcannary.dao.BlockBean
import com.jandroidlibrary.jblockcannary.dao.BlockDataBase

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */
class BlockViewModel : ViewModel() {
    fun queryAllBlock(): LiveData<List<BlockBean>> {
        return BlockDataBase.getInstance().blockDao().queryAllBlock()
    }

    fun deleteAll() {
        BlockDataBase.getInstance().blockDao().deleteAllBlock()
    }


}