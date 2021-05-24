package com.bd.component.perf.monitor.utiils

import android.util.Log
import com.bd.component.perf.monitor.utiils.Constants.PREFIX_TAG

object PerfLog: IPerfLog {
    private var perfLog: IPerfLog? = null

    override fun logI(tag: String, msg: String) {
        val msgTag = PREFIX_TAG + tag
        if (perfLog == null) {
            Log.i(msgTag, msg)
        } else {
            perfLog!!.logI(msgTag, msg)
        }
    }


    override fun logE(tag: String, msg: String) {
        val msgTag = PREFIX_TAG + tag
        if (perfLog == null) {
            Log.e(msgTag, msg)
        } else {
            perfLog!!.logE(msgTag, msg)
        }
    }

    override fun logW(tag: String, msg: String) {
        val msgTag = PREFIX_TAG + tag
        if (perfLog == null) {
            Log.w(msgTag, msg)
        } else {
            perfLog!!.logW(msgTag, msg)
        }
    }
}