package com.inter.trunks.moviedb.base.util

import java.text.SimpleDateFormat
import java.util.*


const val TIME_PATTERN = "dd.MM.yyyy HH:mm"

fun getDateTime(millis: Long = System.currentTimeMillis()) =
    SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        .format(System.currentTimeMillis())