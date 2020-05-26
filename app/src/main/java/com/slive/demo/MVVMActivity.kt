package com.slive.demo

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.slive.demo.mv.MyObserver
import com.slive.demo.mv.MyViewModel
import kotlinx.android.synthetic.main.mvvm_layout.play
import kotlinx.android.synthetic.main.mvvm_layout.watch

class MVVMActivity: AppCompatActivity() {
    private lateinit var mModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mvvm_layout)
        initView()
        mModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        mModel.currentName.observe(this, Observer { newName ->
            android.util.Log.e("MVVMActivity", "newName: $newName")
            play.text = newName
        })
        mModel.currentName.value = "aaaaaa"
        initLifeObserver()
    }

    private fun initView() {
        watch.setOnClickListener {
            Handler().postDelayed( Runnable {
                mModel.currentName.value = "bbbbbb"
                android.util.Log.e("MVVMActivity", "runnable.run")
            }, 5000)
        }
    }

    private fun initLifeObserver() {
        lifecycle.addObserver(MyObserver())
    }

}