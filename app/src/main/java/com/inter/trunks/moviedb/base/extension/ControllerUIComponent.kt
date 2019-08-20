package com.inter.trunks.moviedb.base.extension

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.inter.trunks.moviedb.base.ui.PagerAdapter
import com.inter.trunks.moviedb.base.uicomponent.PagerUIComponent
import com.inter.trunks.moviedb.base.uicomponent.ProgressableUIComponent
import com.inter.trunks.moviedb.base.uicomponent.RecyclerUIComponent
import com.inter.trunks.moviedb.base.uicomponent.ToolbarUIComponent
import com.inter.trunks.presentation.base.component.UIComponentHolder

/**
 * Фукнции-расширения для работы с ComponentHolder-ом.
 */
fun UIComponentHolder.hideProgress() = getComponent(ProgressableUIComponent::class.java)?.end()

fun UIComponentHolder.showProgress() = getComponent(ProgressableUIComponent::class.java)?.begin()

//fun UIComponentHolder.hidePullRefresh() = getComponent(PullableUIComponent::class.java)?.end()

fun UIComponentHolder.endProgress() = progressableComponent().forEach { it.end() }

fun UIComponentHolder.refreshUI() = getComponent(RecyclerUIComponent::class.java)?.refreshUI()

fun <T : Fragment> UIComponentHolder.getPagerFragmentByClass(frClass: Class<T>) =
    getComponent(PagerUIComponent::class.java)?.getPageFragment(frClass)

fun UIComponentHolder.setEnableMenuItem(menuId: Int, enable: Boolean) =
    getComponent(ToolbarUIComponent::class.java)?.disableEnableMenuItem(menuId, enable)

fun UIComponentHolder.setMenuItemVisible(menuId: Int, visible: Boolean) =
    getComponent(ToolbarUIComponent::class.java)?.setMenuItemVisible(menuId, visible)

fun UIComponentHolder.setPagerList(list: MutableList<PagerAdapter.Page>) =
    getComponent(PagerUIComponent::class.java)?.setPages(list)
//
//fun UIComponentHolder.hidePaginationLoader() =
//    getComponent(PaginationUIComponent::class.java)?.end()

fun UIComponentHolder.setProgressViewColor(@ColorRes color: Int) =
    getComponent(ProgressableUIComponent::class.java)?.setProgressViewColor(color)

fun UIComponentHolder.scrollToPosition(position: Int) =
    getComponent(RecyclerUIComponent::class.java)?.scrollToPosition(position)

fun UIComponentHolder.setNeedHideRootView(needHideRootView: Boolean) =
    getComponent(ProgressableUIComponent::class.java)?.setNeedHideRootView(needHideRootView)

fun UIComponentHolder.setEmptyViewText(@StringRes resId:Int) = getComponent(RecyclerUIComponent::class.java)?.setEmptyViewText(resId)

fun UIComponentHolder.setEmptyViewIcon(@DrawableRes drawId: Int) = getComponent(RecyclerUIComponent::class.java)?.setEmptyViewIcon(drawId)

/**
 * Меняет позицию ProgressBar вниз экрана
 */
fun UIComponentHolder.setProgressPaginationPosition(toDown: Boolean) {
    val recycler = getComponent(RecyclerUIComponent::class.java)?.recycler
    val loader = getComponent(ProgressableUIComponent::class.java)?.progressView
    if (recycler != null && loader != null) {
        if (loader.layoutParams is ConstraintLayout.LayoutParams) {
            val layoutParams = loader.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topToBottom = recycler.id
            layoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
            loader.requestLayout()
        }
    }
}