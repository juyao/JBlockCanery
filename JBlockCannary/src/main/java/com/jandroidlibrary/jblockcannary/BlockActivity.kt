package com.jandroidlibrary.jblockcannary

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.jandroidlibrary.jblockcannary.dao.BlockBean
import com.jandroidlibrary.jblockcannary.utils.SpUtils
import java.io.*

class BlockActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    val adapter = BlockAdapter()
    val viewmodel by viewModels<BlockViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)
        supportActionBar?.apply {
            title = "jblock for ${applicationContext.packageName}"
        }
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter
        viewmodel.queryAllBlock().observe(this,
            { t ->
                adapter.dataList = t ?: listOf()
                adapter.notifyDataSetChanged()
            })
        adapter.clickListener = object : BlockItemClickListener {
            override fun click(item: BlockBean) {
                val intent = Intent(this@BlockActivity, BlockDetailActivity::class.java)
                intent.putExtra("block_id", item.id)
                startActivity(intent)
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.block_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                viewmodel.deleteAll()
                true
            }

            R.id.export -> {
                if (adapter.dataList.isNullOrEmpty()) {
                    Toast.makeText(this@BlockActivity, "?????????????????????????????????", Toast.LENGTH_SHORT).show()
                    return true
                }
                val filePath = writeListIntoSDcard(
                    "jblock_${System.currentTimeMillis()}.txt",
                    adapter.dataList
                )
                if (!File(filePath).exists()) {
                    Toast.makeText(this@BlockActivity, "???????????????", Toast.LENGTH_SHORT).show()
                    return true
                }
                var uri: Uri = if (Build.VERSION.SDK_INT >= 24) {//???SDK????????????24  ??????uri????????????????????????
                    FileProvider.getUriForFile(
                        this.applicationContext,
                        "${applicationInfo.packageName}.provider",
                        File(filePath)
                    )
                } else {
                    Uri.fromFile(File(filePath))
                }

                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "?????????"))
                true
            }

            R.id.limit -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("?????????????????????(??????:ms),????????????${SpUtils.getLimit()}ms,??????200ms")
                val edit = EditText(this)
                edit.hint = "?????????ms,??????100???1000??????"
                edit.inputType = InputType.TYPE_CLASS_NUMBER
                builder.setView(edit)
                builder.setPositiveButton(
                    "??????"
                ) { dialog, _ ->
                    if (edit.text.isNullOrEmpty()) {
                        Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        return@setPositiveButton
                    }
                    try {
                        SpUtils.saveLimit(edit.text.toString().toLong())
                        JBlockCanary.minThreshold = edit.text.toString().toLong()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    dialog.dismiss()

                }
                builder.setNegativeButton(
                    "??????"
                ) { dialog, _ -> dialog.dismiss() }
                builder.setCancelable(true)
                builder.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun writeListIntoSDcard(fileName: String, list: List<BlockBean>): String {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            val sdCardDir = getExternalFilesDir("block_record")
            if (sdCardDir?.exists() == false) {
                sdCardDir.mkdir()
            }
            val sdFile = File(sdCardDir, fileName);
            return try {
                list.forEach {
                    write(sdFile, content = it.toString(), "UTF-8", true)
                }
                sdFile.absolutePath
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
                "";
            } catch (e: IOException) {
                e.printStackTrace();
                ""
            }
        } else {
            return ""
        }
    }


    @Throws(IOException::class)
    fun write(target: File, content: String?, encoding: String?, append: Boolean) {
        var writer: BufferedWriter? = null
        try {
            if (!target.parentFile.exists()) {
                target.parentFile.mkdirs()
            }
            writer = BufferedWriter(
                OutputStreamWriter(
                    FileOutputStream(target, append), encoding
                )
            )
            writer.write(content)
        } finally {
            writer?.close()
        }
    }


}