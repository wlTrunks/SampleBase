/*
 * Copyright (c) 2019, TeamDirector LLC. All rights reserved.
 */

package com.inter.trunks.presentation.base.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * Hide keyboard
 */
fun Fragment.hideKeyboard() {
    context?.let { context ->
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            view?.let {
                this.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }
}