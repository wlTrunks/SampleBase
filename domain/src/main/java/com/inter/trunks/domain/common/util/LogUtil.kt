package com.inter.trunks.domain.common.util

import java.lang.Exception


fun Exception.toLogcat(tag: String, isDebug: Boolean = true) {
    if (isDebug) {
        println("$tag Begin stacktrace")
        this.printStackTrace()
    }
}

fun String.toLogcat(tag: String, isDebug: Boolean = true) {
    if (isDebug) println("$tag: $this")
}