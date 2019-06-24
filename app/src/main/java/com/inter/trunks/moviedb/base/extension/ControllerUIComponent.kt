package com.inter.trunks.moviedb.base.extension

import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.inter.trunks.moviedb.base.uicomponent.ProgressableUIComponent
import com.inter.trunks.moviedb.base.uicomponent.RecyclerUIComponent
import com.inter.trunks.presentation.base.component.UIComponentHolder

/**
 * Фукнции-расширения для работы с ComponentHolder-ом.
 */
fun UIComponentHolder.hideProgress() = getComponent(ProgressableUIComponent::class.java)?.end()

fun UIComponentHolder.showProgress() = getComponent(ProgressableUIComponent::class.java)?.begin()

//fun UIComponentHolder.hidePullRefresh() = getComponent(PullableUIComponent::class.java)?.end()

fun UIComponentHolder.endProgress() = progressableComponent().forEach { it.end() }

fun UIComponentHolder.refreshUI() = getComponent(RecyclerUIComponent::class.java)?.refreshUI()

//fun UIComponentHolder.setPagerList(list: List<PagerAdapter.Page>) =
//    getComponent(PagerUIComponent::class.java)?.setPages(list)
//
//fun UIComponentHolder.hidePaginationLoader() =
//    getComponent(PaginationUIComponent::class.java)?.end()

fun UIComponentHolder.setProgressViewColor(@ColorRes color: Int) =
    getComponent(ProgressableUIComponent::class.java)?.setProgressViewColor(color)

fun UIComponentHolder.scrollToPosition(position: Int) =
    getComponent(RecyclerUIComponent::class.java)?.scrollToPosition(position)

fun UIComponentHolder.setNeedHideRootView(needHideRootView: Boolean) =
    getComponent(ProgressableUIComponent::class.java)?.setNeedHideRootView(needHideRootView)

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