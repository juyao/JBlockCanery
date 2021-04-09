package com.jandroidlibrary.jblockcannary

import androidx.lifecycle.ViewModel
import com.jandroidlibrary.jblockcannary.dao.BlockBean
import com.jandroidlibrary.jblockcannary.dao.BlockDataBase

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */
class DetailViewModel : ViewModel() {

    fun getBlockBean(id: Long): BlockBean {
        return BlockDataBase.getInstance().blockDao().findBlock(id)
    }
}