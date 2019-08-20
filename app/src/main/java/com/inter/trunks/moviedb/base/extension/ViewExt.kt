package com.inter.trunks.moviedb.base.extension

import android.app.Activity
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner

/**EXT FOR View Generic */
fun <V : View> V.showWithAction(show: Boolean, action: (V.() -> Unit)? = null): V {
    visibility = if (show) View.VISIBLE else View.GONE
    action?.let { it() }
    return this
}

fun <V : View> V.show(show: Boolean) = showWithAction(show)

fun <V : View> V.setViewClickableFocusable(enable: Boolean) {
    this.isFocusable = enable
    this.isClickable = enable
}

fun <V : View> V.setClickableNotFocusable(enable: Boolean) {
    this.isEnabled = enable
    this.isClickable = enable
    this.isFocusable = false

}

fun <V : View> V.showKeyboard() {
    this.requestFocus()
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

/**EXT FOR Spinner */
/** Возвращение позиции элемента в спинере по условию */
fun Spinner.getPredicateSPItemPosition(
    predicate: (data: Any?) -> Boolean
): Int {
    for (i in 0 until this.count) {
        if (predicate(this.getItemAtPosition(i).toString())) {
            return i
        }
    }
    return 0
}

/**EXT FOR EditText */
fun EditText.setMultiLine() {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
}



