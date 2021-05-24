package com.slive.demo.mv

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class MyViewModel2(activity: AppCompatActivity) {
    val viewModel: MyViewModel

    init {
        viewModel = ViewModelProviders.of(activity).get(MyViewModel::class.java)
    }

    fun updateData() {
        viewModel.currentName.value = "MyViewModel2"
    }
}