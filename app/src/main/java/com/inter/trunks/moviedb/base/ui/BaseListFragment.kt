package com.inter.trunks.moviedb.base.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inter.trunks.moviedb.base.uicomponent.RecyclerUIComponent
import com.inter.trunks.presentation.base.component.UIComponent

/**
 * Базовый класс со списком.
 */
abstract class BaseListFragment : BaseFragment() {

    protected var adapter: BaseRecyclerAdapter? = null

    override fun getComponentUIList(): List<UIComponent>? = listOf(
            RecyclerUIComponent(adapter)
                    .apply {
                        layoutManager = getRecyclerLayoutManager()
                        recyclerItemDecoration.addAll(getItemDecorationList())
                    }
    )

    /**
     * Функиця для создания ViewHolder
     */
    abstract fun getViewHolders(): ((ViewGroup, Int) -> RecyclerView.ViewHolder)

    /**
     * Функция для определения типа ViewHolder, если в списке мультитайп
     */
    open fun getItemViewType(): ((Int, Any) -> Int)? = null

    /**
     * Список декораций для recyclerView
     */
    protected open fun getItemDecorationList(): HashSet<RecyclerView.ItemDecoration> = hashSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = initAdapter()
        addItemsClickEventsListener()?.let { adapter?.listenersList?.add(it) }
        super.onCreate(savedInstanceState)
    }

    @NonNull
    protected open fun initAdapter() = BaseRecyclerAdapter().apply {
        setVHFunction(getViewHolders(), getItemViewType())
    }

    @NonNull
    protected open fun getRecyclerLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    open fun addItemsClickEventsListener(): ItemsClickEventsListener? = null

    override fun onDestroy() {
        super.onDestroy()
        adapter = null //memory leak?!
    }

}
