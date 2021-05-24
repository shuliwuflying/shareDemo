package com.slive.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import com.slive.demo.services.MyAccessibility
import com.slive.demo.utils.NativeHook
import kotlinx.android.synthetic.main.mvvm_layout.image_view1
import kotlinx.android.synthetic.main.mvvm_layout.play
import kotlinx.android.synthetic.main.mvvm_layout.test
import kotlinx.android.synthetic.main.mvvm_layout.watch
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.RandomAccessFile

class MVVMActivity: BaseActivity(){
//    private lateinit var mModel: MyViewModel
    private val PATH = "/sdcard/相机/"
    private val TAG = "MVVMActivity"
    private val uiHandler = Handler()
    private val handleThread = HandlerThread("handle-test")
    private var handlerWork: Handler? = null
    private var workRunnable: Runnable? = null
    private var uiRunnable:Runnable? = null
    private var count = 0
    private var testValue: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mvvm_layout)
        initView()
//        Hacker.hook()
        initLifeObserver()
        renameAndCopy()


        uiRunnable = object : Runnable {
            override fun run() {
                android.util.Log.e("sliver", "55555555 ui Runnable")
                try {
                    Thread.sleep(200)
                } catch (e: java.lang.Exception) {

                }
            }
        }

        workRunnable = object : Runnable {
            override fun run() {
                while(count < 20) {
                    uiHandler.post(uiRunnable)
                    try {
                        Thread.sleep(200)
                    } catch (e: java.lang.Exception) {

                    }
                    count ++
                }
            }
        }
    }




    private fun initView() {
        var value = "test"
        var value1 = "test1"
        var value2 = "test2"

        watch.setOnClickListener {
            android.util.Log.e("sliver","testValue: ${testValue!!}")
        }
        test.setOnClickListener {
            NativeHook.nativeTestAdd()
            android.util.Log.e("sliver","test onClick")
        }


        image_view1.setOnClickListener {
            try {
                android.util.Log.e("sliver","testValue: ${testValue!!}")
            } catch (e: Exception) {

            }
        }

    }

    private fun addRunnable() {
        uiHandler.postAtFrontOfQueue {
            android.util.Log.e("sliver", "addRunnable")
            addRunnable()
        }
    }

    fun getTextViewTitle(): String {
        return "BBBBBB"
    }

    private fun initLifeObserver() {
//        lifecycle.addObserver(MyObserver())
    }


    private fun renameAndCopy() {
        val file = File(PATH)
        if (file.isDirectory && file.exists()) {
            val files = file.listFiles()
            files.forEach {
                if (it.absolutePath.endsWith(".mp4")) {
                    val index = it.absolutePath.lastIndexOf(File.separator)
                    val path = it.absolutePath.substring(0, index)
                    val name = it.absolutePath.substring(index + 1)
                    if (!name.startsWith("beauty")) {
                        val targetFile = File(path + File.separator+ "beauty_"+name)
                        it.renameTo(targetFile)
                        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        val uri = Uri.fromFile(targetFile)
                        scanIntent.data = uri
                        sendBroadcast(scanIntent)
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            android.util.Log.e("sliver", android.util.Log.getStackTraceString(Throwable("onWindowFocusChanged")))
        }
    }


    override fun onStart() {
        super.onStart()
        Log.e("sliver", "onStart111111")
    }

    override fun onStop() {
        Log.e("sliver", "onStop111111")
        try {
//            Thread.sleep(60 * 1000.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("sliver", "onStop222222")
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
        android.util.Log.e("sliver", "onDestroy this: ${MVVMActivity@this}")
    }
}

internal class SurfaceHolderTest : SurfaceHolder.Callback {

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
//        android.util.Log.e("sliver", "surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
//        android.util.Log.e("sliver", "surfaceDestroyed")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
//        android.util.Log.e("sliver", "surfaceCreated")
    }

}