/*
 * Copyright (c) 2019, TeamDirector LLC. All rights reserved.
 */

package com.inter.trunks.presentation.base.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * If Boolean true invoke block
 */
inline fun <T> Boolean.isSuccess(block: () -> T) = if (this) block() else null

/**
 * If Boolean false invoke block
 */
inline fun <T> Boolean.isNotSuccess(block: () -> T) = if (!this) block() else null

/**
 * Round double format
 */
fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toDouble()
}

fun String.toNumericString(): Int = this.filter { it.isDigit() }.toInt()
