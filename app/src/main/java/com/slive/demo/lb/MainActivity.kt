package com.slive.demo.lb

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.slive.demo.R

class MainActivity : AppCompatActivity() {

//    val runnableLambda: ()->Unit = { android.util.Log.e("sliver","run")}
    val runnable = Runnable {
        android.util.Log.e("sliver","run")
    }
    val runnableObject = object:Runnable {
        override fun run() {
            android.util.Log.e("sliver","run")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_test_run)
        initView()
    }

    private fun initView() {
        val addRunnable:TextView = findViewById(R.id.add_runnable)
        addRunnable.setOnClickListener{
            JavaRunQueue.addRunnable(runnable)
            JavaRunnable2.addRunnable(runnableObject)
            Log.e("sliver","JavaRunQueue.size: "+JavaRunQueue.size())
            Log.e("sliver","JavaRunnable2.size: "+ JavaRunnable2.size())
        }

        val removeRunnable: TextView = findViewById(R.id.remove_runnable)
        removeRunnable.setOnClickListener {
            JavaRunQueue.removeRunnable(runnable)
            JavaRunnable2.removeRunnable(runnableObject)
            Log.e("sliver","JavaRunQueue.size: "+JavaRunQueue.size())
            Log.e("sliver","JavaRunnable2.size: "+ JavaRunnable2.size())
        }
    }
}
