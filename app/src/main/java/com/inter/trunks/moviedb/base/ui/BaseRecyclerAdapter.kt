package com.inter.trunks.moviedb.base.ui

import android.database.Observable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * Базовый адаптер.
 *
 * @property createViewHolder - требуется определить.
 */
open class BaseRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listenersList = HashSet<ItemsClickEventsListener>()

    open var itemsClickEventsObservable = ItemsClickEventsObservable()

    protected open var createViewHolder: ((ViewGroup, Int) -> RecyclerView.ViewHolder)? = null

    protected open var getItemViewType: ((Int, Any) -> Int)? = null

    var clearListonDetach = true

    /** Данные, которые необходимо будет отобразить в списке. */
    private val itemList: MutableList<Any> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (createViewHolder == null) throw RuntimeException("createViewHolder must be initialized")
        val vh = createViewHolder?.invoke(parent, viewType)
            ?: throw RuntimeException("ViewHolder not have been setted in createViewHolder")
        (vh as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).processOnClickListener(
            vh,
            itemsClickEventsObservable
        )
        (vh as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).processOnLongClickListener(
            vh,
            itemsClickEventsObservable
        )
        return vh
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).onBindViewHolder(
            viewHolder,
            itemList[viewHolder.adapterPosition]
        )
    }

    fun setVHFunction(
        createViewHolder: ((ViewGroup, Int) -> RecyclerView.ViewHolder),
        getItemViewType: ((Int, Any) -> Int)? = null
    ) {
        this@BaseRecyclerAdapter.createViewHolder = createViewHolder
        this@BaseRecyclerAdapter.getItemViewType = getItemViewType
    }

    override fun getItemCount() = itemList.size

    /** Регистрация слушателей. */
    protected open fun registerItemsEventsListener() {
        for (rvmItemsClickEventsListener in listenersList) {
            itemsClickEventsObservable.registerObserver(rvmItemsClickEventsListener)
        }
    }

    protected open fun unRegisterAllObservers() = itemsClickEventsObservable.unregisterAll()

    /** Возвращаем элемент из списка по его позиции. */
    fun getItemAtPosition(position: Int): Any? = itemList.getOrNull(position)

    /** Добавление данных в список. */
    open fun setData(items: List<Any>) {
        with(itemList) {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
        getItemViewType?.invoke(position, itemList[position]) ?: 0

    /** Добавление и дополнение данных в текущий список. */
    open fun addData(items: List<Any>) {
        val positionStart = itemList.size + 1
        this.itemList.addAll(items)
        notifyItemRangeInserted(positionStart, itemList.size)
    }

    fun addData(position: Int, list: List<Any>) {
        this.itemList.addAll(position, list)
        notifyItemRangeInserted(position, position + list.size)
    }

    fun addData(position: Int, item: Any) {
        this.itemList.add(position, item)
        notifyItemInserted(position)
    }

    open fun addData(item: Any) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    /**
     * Обновление данных в текущем списке. В качестве best pratics используется зарекоммендованный
     * класс для таких целей - DiffUtil. Из-за этого каждый дочерний адаптер должен переопределять
     * данный метод, т.к. [DiffUtil.Callback] у каждого свой.
     */
    open fun update(items: List<Any>) = Unit

    /**
     * Удаление элемента по позиции.
     */
    @Suppress("UNCHECKED_CAST")
    open fun <T> removeItemAtPosition(position: Int): T {
        var item: T? = null
        if (itemList.contains(itemList[position])) {
            item = itemList[position] as T
            itemList.remove(itemList[position])
            notifyItemRemoved(position)
        }
        return item as T
    }

    fun removeAt(position: Int): Any {
        val item: Any = itemList.removeAt(position)
        notifyItemRemoved(position)
        return item
    }

    fun remove(item: Any): Boolean {
        val isRemoved = itemList.remove(item)
        if (isRemoved) notifyDataSetChanged()
        return isRemoved
    }

    /** Удаление элемента по кастомному предикату. */
    @Suppress("UNCHECKED_CAST")
    open fun <T> removeItemAtPredicate(predicate: (data: Any?) -> Boolean): T {
        var item: T? = null
        itemList.find(predicate)?.let {
            item = it as T
            val index = itemList.indexOf(it)
            itemList.remove(it)
            notifyItemRemoved(index)
        }
        return item as T
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun resetData(notify: Boolean = false) {
        itemList.clear()
        if (notify) notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getList(): List<T> = itemList as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getItem(position: Int): T = itemList[position] as T

    @Suppress("UNCHECKED_CAST")
    fun <T> getItemPredicate(predicate: (data: Any?) -> Boolean): T? = itemList.find(predicate) as T

    fun listContainItem(item: Any): Boolean = itemList.contains(item)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        unRegisterAllObservers()
        if (clearListonDetach) itemList.clear()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        registerItemsEventsListener()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).onViewDetachedFromWindow(
            holder
        )
    }

    interface ItemViewHolderBinder<T, VH : RecyclerView.ViewHolder> {

        fun onBindViewHolder(holder: VH, item: T)

        fun processOnClickListener(
            viewHolder: VH,
            itemsClickEventsObservable: ItemsClickEventsObservable
        ) {
            viewHolder.itemView.setOnClickListener {
                itemsClickEventsObservable.notifyItemClick(viewHolder.adapterPosition)
            }
        }

        fun processOnLongClickListener(
            viewHolder: VH,
            itemsClickEventsObservable: ItemsClickEventsObservable
        ) {
            viewHolder.itemView.setOnLongClickListener {
                itemsClickEventsObservable.notifyItemLongClick(viewHolder.adapterPosition)
            }
        }

        fun onViewDetachedFromWindow(viewHolder: VH) {}
    }

    open class ItemsClickEventsObservable : Observable<ItemsClickEventsListener>() {

        fun notifyItemClick(position: Int) = synchronized(mObservers) {
            for (l in mObservers) l.onItemClick(position)
        }

        fun notifyItemLongClick(position: Int): Boolean = synchronized(mObservers) {
            var consumed = false
            for (l in mObservers) if (l.onItemLongClick(position)) {
                consumed = true
            }
            return consumed
        }
    }

    /** Базовый VH с использованием синтетики. */
    abstract class BaseViewHolder<T, VH : RecyclerView.ViewHolder>(
        val layoutId: Int,
        val parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    ), ItemViewHolderBinder<T, VH>, LayoutContainer {

        override val containerView: View = itemView
    }
}

interface ItemsClickEventsListener {

    fun onItemClick(position: Int) {}

    /** @return возращать true для получения. */
    fun onItemLongClick(position: Int): Boolean = false
}

