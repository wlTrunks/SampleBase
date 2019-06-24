package com.inter.trunks.moviedb.base.extension

import android.view.View

fun <V : View> V.showWithAction(show: Boolean, action: (V.() -> Unit)? = null): V {
    visibility = if (show) View.VISIBLE else View.GONE
    action?.let { it() }
    return this
}

fun <V : View> V.show(show: Boolean) = showWithAction(show)