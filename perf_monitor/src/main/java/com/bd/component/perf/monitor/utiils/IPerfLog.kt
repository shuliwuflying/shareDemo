package com.bd.component.perf.monitor.utiils

interface IPerfLog {
    fun logI(tag: String, msg: String)

    fun logE(tag: String, msg: String)

    fun logW(tag: String, msg: String)
}