package com.inter.trunks.moviedb.base.uicomponent

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inter.trunks.moviedb.base.extension.show
import com.inter.trunks.presentation.base.component.Refreshable
import com.inter.trunks.presentation.base.component.UIComponent
import org.jetbrains.anko.findOptional

/**
 * Компонент для работы со списком RecyclerView.
 *
 * RecyclerView - android.R.id.list.
 * EmptyView - android.R.id.empty. Будет показываться, если список пуст.
 *
 */
class RecyclerUIComponent<Adapter : RecyclerView.Adapter<*>>(
    var adapter: Adapter? = null
) : UIComponent(), Refreshable {
    var recycler: RecyclerView? = null

    /**
     * Отображается View-в случае пустого результата.
     */
    private var emptyView: View? = null

    var recyclerItemDecoration: MutableSet<RecyclerView.ItemDecoration> = mutableSetOf()

    var layoutManager: RecyclerView.LayoutManager? = null

    override fun onViewCreated(layout: View) {
        super.onViewCreated(layout)
        layout?.let { view ->
            recycler = view.findOptional(android.R.id.list)
            emptyView = view.findOptional(android.R.id.empty)
        }
        emptyView?.show(false)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recycler?.let { list ->
            with(list) {
                if (this@RecyclerUIComponent.layoutManager == null)
                    this@RecyclerUIComponent.layoutManager = LinearLayoutManager(context)

                layoutManager = this@RecyclerUIComponent.layoutManager

                adapter = this@RecyclerUIComponent.adapter

                recyclerItemDecoration.forEach { addItemDecoration(it) }
            }
        }
    }

    override fun refreshUI() {
        adapter?.let { adapter ->
            if (adapter.itemCount > 0)
                hideEmptyView()
            else
                showEmptyView()
        } ?: showEmptyView()
    }

    private fun showEmptyView() {
        recycler?.show(false)
        emptyView?.show(true)
    }

    private fun hideEmptyView() {
        recycler?.show(true)
        emptyView?.show(false)
    }

    fun hideViews() {
        recycler?.show(false)
        emptyView?.show(false)
    }

    override fun onDestroy() {
        clear()
        recyclerItemDecoration.clear()
        adapter = null
        layoutManager = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        clear()
        super.onDestroyView()
    }

    private fun clear() {
        recycler?.let {
            it.adapter = null
            it.layoutManager = null
        }
        recycler = null
    }

    fun setEmptyViewText(@StringRes stringId: Int) {
        (emptyView as? TextView)?.text = context?.getString(stringId)
    }

    fun setEmptyViewIcon(@DrawableRes drawId: Int) {
        (emptyView as? TextView)?.setCompoundDrawablesWithIntrinsicBounds(0, drawId, 0, 0)
    }

    fun scrollToPosition(position: Int) = recycler?.scrollToPosition(position)

}