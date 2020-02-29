package com.sirius.miracletools

import android.util.Log

class Logger {
    private val _tag = Logger::class.java.simpleName

    fun log(tag: String, message: String) {
        Log.d(_tag, "$tag: $message")
    }
}