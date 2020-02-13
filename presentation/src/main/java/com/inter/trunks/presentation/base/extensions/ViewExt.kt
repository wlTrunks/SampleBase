/*
 * Copyright (c) 2019, TeamDirector LLC. All rights reserved.
 */

package com.inter.trunks.presentation.base.extensions

import android.view.View

/**
 * Extension for setting view visibility
 * @param showView true for visible false gone
 * @param block additional action optional
 */
fun View.showView(showView: Boolean, block: (() -> Unit)? = null) {
    this.visibility = if (showView) View.VISIBLE else View.GONE
    block?.invoke()
}

fun View.setClickableFocusable(boolean: Boolean, block: (() -> Unit)? = null) {
    this.isClickable = boolean
    this.isFocusable = boolean
    this.isEnabled = boolean
}

/**
 * Function set layout right to left or overwise
 * @param isRtL example Locale.getDefault().isO3Language == "heb"
 */
fun View.setRightToLeft(isRtL: Boolean) {
    this.layoutDirection = if (isRtL) View.LAYOUT_DIRECTION_RTL
    else View.LAYOUT_DIRECTION_LTR
}
