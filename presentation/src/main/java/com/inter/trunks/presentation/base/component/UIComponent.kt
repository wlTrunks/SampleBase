package com.inter.trunks.presentation.base.component

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper


abstract class UIComponent {
    protected var context: Context? = null
        private set

    fun onViewCreated(context: Context, layout: View) {
        this.context = context
        onViewCreated(layout)
    }

    open fun onViewCreated(layout: View) {}

    open fun onDestroyView() {}

    @CallSuper
    open fun onDestroy() {
        this.context = null
    }
}