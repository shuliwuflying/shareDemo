package com.slive.demo.hook

class TestObj {

    fun addNum():Unit {
        var run: Runnable? = null
        android.util.Log.e("sliver", "addNum11111111111")
        android.util.Log.e("sliver", "run: ${run!!}")
    }
}