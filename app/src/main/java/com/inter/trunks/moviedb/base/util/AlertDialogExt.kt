package com.inter.trunks.moviedb.base.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.showAlertDialog(
    title: Int = 0,
    message: String? = null,
    cancelable: Boolean = false,
    cancelableTouchOutside: Boolean = false,
    builderFunction: AlertDialog.Builder.() -> Any
): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.builderFunction()
    val dialog = builder.create()

    if (title != 0)
        dialog.setTitle(title)
    dialog.setMessage(message)
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
    try {
        dialog.show()
    } catch (e: Exception) {
        println("showAlertDialog e: ${e.message}")
    }
    return dialog
}


fun AlertDialog.Builder.positiveButton(text: String = "Ок", handleClick: (i: Int) -> Unit = {}) {
    this.setPositiveButton(text, { dialogInterface, i -> handleClick(i) })
}

fun AlertDialog.Builder.negativeButton(
    text: String = "Отмена",
    handleClick: (i: Int) -> Unit = {}
) {
    this.setNegativeButton(text, { dialogInterface, i -> handleClick(i) })
}

fun AlertDialog.Builder.neutralButton(text: String, handleClick: (i: Int) -> Unit = {}) {
    this.setNeutralButton(text, { dialogInterface, i -> handleClick(i) })
}

