/*
 * Copyright (c) 2019, TeamDirector LLC. All rights reserved.
 */

package com.inter.trunks.presentation.base.extensions

import com.inter.trunks.presentation.BuildConfig

fun String.toLog(tag: String) {
    if (BuildConfig.DEBUG) {
        println("$tag $this")
    }
}

fun Throwable.toLog(tag: String) {
    if (BuildConfig.DEBUG) {
        println("$tag")
    }
}

fun checkThread(msg: String) {
    if (BuildConfig.DEBUG) {
        println("THREAD TEST $msg I AM ${Thread.currentThread().name}")
    }
}