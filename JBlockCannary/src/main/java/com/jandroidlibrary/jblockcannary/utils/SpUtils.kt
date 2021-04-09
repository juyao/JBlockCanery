package com.jandroidlibrary.jblockcannary.utils

import android.content.Context
import com.jandroidlibrary.jblockcannary.JBlockCanary

/**
日期 :4/9/21
作者: juyao
描述：
版本：
 */
class SpUtils {
    companion object {
        val SP_NAME = "sp_block"
        val LIMIT_KEY = "block_limit"
        fun saveLimit(value: Long) {
            val sp = JBlockCanary.mContext!!.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putLong(LIMIT_KEY, value)
            editor.commit()
        }

        fun getLimit(): Long {
            val sp = JBlockCanary.mContext!!.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return sp.getLong(LIMIT_KEY, 200L)
        }
    }
}