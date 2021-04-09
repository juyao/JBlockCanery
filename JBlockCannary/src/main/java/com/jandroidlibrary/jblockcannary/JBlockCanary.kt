package com.jandroidlibrary.jblockcannary

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.jandroidlibrary.jblockcannary.dao.BlockBean
import com.jandroidlibrary.jblockcannary.dao.BlockDataBase
import com.jandroidlibrary.jblockcannary.utils.SpUtils
import java.text.SimpleDateFormat
import java.util.*

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */
class JBlockCanary {
    companion object {
        val TAG = "JBlockCanary"
        val SEPARATOR = "\r\n"
        var minThreshold = 200L
        var mContext: Context? = null
        fun init(context: Context) {
            mContext = context
            minThreshold = SpUtils.getLimit()
            initDelayHandler()
            initPrinter()
        }

        //使用 printerStart 进行判断是方法执行前还是执行后调用 println
        private var printerStart = true

        //记录方法执行前的时间
        private var printerStartTime = 0L
        private fun initPrinter() {
            Looper.getMainLooper().setMessageLogging {
                if (printerStart) {
                    printerStart = false
                    printerStartTime = System.currentTimeMillis()
                    delayHandler.removeCallbacks(runnable)
                    delayHandler.postDelayed(runnable, (minThreshold * 0.8).toLong())
                } else {
                    printerStart = true
                    delayHandler.removeCallbacks(runnable)
                    (System.currentTimeMillis() - printerStartTime).let {
                        if (it > minThreshold) {
                            Log.i(TAG, "主线程发生了卡顿，卡顿时长：$it ms")
                            Log.i(TAG, "堆栈信息：$stringBuilder")
                            Toast.makeText(mContext, "检测到一处卡顿，请前往jblock查看", Toast.LENGTH_SHORT)
                                .show()
                            val blockBean = BlockBean()
                            blockBean.blockTime = it
                            blockBean.methodName = stringBuilder.split(SEPARATOR)[0].toString()
                            blockBean.date = dateFormat(Date())
                            blockBean.stackInfo = stringBuilder.toString()
                            BlockDataBase.getInstance().blockDao().saveBlock(blockBean)
                            stringBuilder.clear()
                        }

                    }
                }
            }
        }

        fun dateFormat(date: Date): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            val dateString = formatter.format(date);
            return dateString
        }

        lateinit var delayHandler: Handler
        private fun initDelayHandler() {
            //让handler的消息在子线程中运行
            val handlerThread = HandlerThread("DelayThread")
            handlerThread.start()
            delayHandler = Handler(handlerThread.looper)
        }

        //获取栈信息进行记录
        private val stringBuilder = StringBuilder()
        private val runnable = Runnable {
            for (stackTraceElement in Looper.getMainLooper().thread.stackTrace) {
                stringBuilder
                    .append(stackTraceElement.toString())
                    .append(SEPARATOR)
            }

        }


    }

}