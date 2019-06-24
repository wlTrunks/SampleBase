package com.inter.trunks.moviedb.base.uicomponent

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inter.trunks.moviedb.base.extension.show
import com.inter.trunks.presentation.base.component.Progressable
import com.inter.trunks.presentation.base.component.UIComponent
import org.jetbrains.anko.findOptional

class PaginationUIComponent(

    private val paginationUIInterface: PaginationUIInterface = PaginationUIInterface.STUB

) : UIComponent(),
    Progressable {

    private var nestedScrollView: NestedScrollView? = null
    private var loader: ProgressBar? = null
    private var nestedScrollViewFocus = FOCUS_NONE
    private var isRequestLoading: Boolean = false

    private val scrollable: RecyclerView.OnScrollListener = RecyclerScrollableController(
        object : RecyclerScrollableController.OnLastItemVisibleListener {
            override fun onLastItemVisible() {
                proceedPaging()
            }
        }
    )

    private val nestedScrollable: NestedScrollView.OnScrollChangeListener =
        NestedScrollView.OnScrollChangeListener { nestedScrollView1, _, scrollY, _, oldScrollY ->
            if (nestedScrollView1.getChildAt(nestedScrollView1.childCount - 1) != null) {
                if (((scrollY >= (nestedScrollView1
                        .getChildAt(nestedScrollView1.childCount - 1)
                        .measuredHeight - nestedScrollView1.measuredHeight)) &&
                            scrollY > oldScrollY)
                ) {
                    proceedPaging()
                }
            }
        }

    override fun onViewCreated(layout: View) {
        super.onViewCreated(layout)
        val recyclerView = layout.findOptional<RecyclerView>(android.R.id.list)
//        nestedScrollView = layout.findOptional(R.id.nestedScroll)
//        loader = layout.findOptional(R.id.recyclerLoader)
        if (nestedScrollView != null) {
            recyclerView?.let {
                ViewCompat.setNestedScrollingEnabled(it, false)
            }
            nestedScrollView?.setOnScrollChangeListener(nestedScrollable)
        } else {
            recyclerView?.addOnScrollListener(scrollable)
        }
    }

    override fun begin() {
        isRequestLoading = true
        loader?.show(true)
    }

    override fun end() {
        loader?.show(false)
        isRequestLoading = false
    }

    /**
     * В случае, если у экрана реализован интерфейс для PaginationViewLoader, то проверяем,
     * что идет запрос на получение данных. В самих запросах при получении информации
     * необходимо переключить флаг для isRequestLoading = false.
     */
    private fun proceedPaging() {
        if (paginationUIInterface.checkPagingEnable() && !isRequestLoading) {
            begin()
            paginationUIInterface.onLoadingNextPage()
            proceedNestedScroll()
        }
    }

    fun setNestedScrollViewFocus(
        @NestedScrollFocusDirection nestedScrollViewFocus: Int
    ): PaginationUIComponent {
        this.nestedScrollViewFocus = nestedScrollViewFocus
        return this
    }

    private fun proceedNestedScroll() = nestedScrollView?.let {
        if (nestedScrollViewFocus != FOCUS_NONE)
            nestedScrollView?.post {
                nestedScrollView?.fullScroll(nestedScrollViewFocus)
            }
    }


    @Retention(AnnotationRetention.SOURCE)
    @IntDef(View.FOCUS_DOWN, View.FOCUS_UP, FOCUS_NONE)
    annotation class NestedScrollFocusDirection

    /**
     * Интерфейс, который нужно реализовывать всем классам, которые будут использовать
     * PaginationUIComponent.
     */
    interface PaginationUIInterface {

        fun onLoadingNextPage()

        fun checkPagingEnable(): Boolean

        companion object {

            val STUB: PaginationUIInterface = object : PaginationUIInterface {

                override fun onLoadingNextPage() {}

                override fun checkPagingEnable(): Boolean = true

            }
        }
    }

    internal class RecyclerScrollableController(private val listener: OnLastItemVisibleListener?) :
        RecyclerView.OnScrollListener() {

        private var previousTotal = 0
        private var loading = true

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val visibleItemCount = recyclerView.childCount
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                val isLastItemVisible =
                    totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD
                if (isLastItemVisible) {
                    listener?.onLastItemVisible()
                    loading = true
                }
            }
        }

        interface OnLastItemVisibleListener {
            fun onLastItemVisible()
        }

        companion object {
            private const val VISIBLE_THRESHOLD = 1
        }
    }

    companion object {
        const val FOCUS_NONE = -1
    }
}
