package com.inter.trunks.moviedb.base.uicomponent

import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import com.inter.trunks.moviedb.R
import com.inter.trunks.presentation.base.component.UIComponent
import org.jetbrains.anko.findOptional

class ToolbarUIComponent(
    @MenuRes private val menuRes: Int,
    private val listener: Toolbar.OnMenuItemClickListener
) : UIComponent() {
    private var toolbar: Toolbar? = null
    override fun onViewCreated(layout: View) {
        super.onViewCreated(layout)
//        toolbar = layout.findOptional(R.id.toolbar)
        toolbar?.let {
            it.inflateMenu(menuRes)
            it.setOnMenuItemClickListener(listener)
        }
    }

    fun disableEnableMenuItem(menuId: Int, enable: Boolean) {
        toolbar?.let {
            val menu = it.menu?.findItem(menuId)
            menu?.let { m ->
                m.setEnabled(enable)
            }
        }
    }

    fun setMenuItemVisible(menuId: Int, visible: Boolean) {
        toolbar?.let {
            val menu = it.menu?.findItem(menuId)
            menu?.let { m ->
                m.setVisible(visible)
            }
        }
    }
}
