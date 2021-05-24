package com.slive.demo.utils

object BLog {

    @JvmStatic
    fun e(tag: String, msg: String) {
        System.out.println("$tag : $msg")
    }
}