package com.jandroidlibrary.jblockcannary

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jandroidlibrary.jblockcannary.databinding.ActivityBlockDetailBinding

class BlockDetailActivity : AppCompatActivity() {
    val viewmodel: DetailViewModel by viewModels()
    var id: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBlockDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "block for detail"
            setDisplayHomeAsUpEnabled(true)

        }

        id = intent.getLongExtra("block_id", 0L)
        val block = viewmodel.getBlockBean(id)
        binding.tvBlockdate.text = "卡顿日期：\n${block.date}"
        binding.tvBlocktime.text = "卡顿时间：\n${block.blockTime}ms"
        binding.tvStackinfo.text = "堆栈信息：\n${block.stackInfo}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)

    }
}