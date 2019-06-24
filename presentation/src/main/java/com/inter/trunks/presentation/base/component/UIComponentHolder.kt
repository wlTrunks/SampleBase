package com.inter.trunks.presentation.base.component

import android.content.Context
import android.view.View
import androidx.collection.ArrayMap


class UIComponentHolder private constructor(components: MutableList<UIComponent>) :
    ComponentHolder {
    private val hashComponent: ArrayMap<Class<UIComponent>, UIComponent> = ArrayMap()

    init {
        for (component in components) {
            hashComponent[component.javaClass] = component
        }
        components.clear()
    }

    fun progressableComponent(): List<Progressable> {
        val list = mutableListOf<Progressable>()
        hashComponent.forEach { if (it.value is Progressable) list.add(it.value as Progressable)}
        return list
    }

    fun onViewCreated(context: Context, view: View) {
        hashComponent.forEach { it.value.onViewCreated(context, view) }
    }

    fun onDestroyView() {
        hashComponent.forEach { it.value.onDestroyView() }
    }

    fun onDestroy() {
        hashComponent.forEach { it.value.onDestroy() }
        hashComponent.clear()
    }

    override fun <T : UIComponent> getComponent(componentClass: Class<T>): T? {
        val component = getComponentByClass(componentClass)
        return if (componentClass.isInstance(component)) {
            component as T
        } else null
    }

    fun <T> getComponentByClass(componentClass: Class<T>): Any? =
        hashComponent[componentClass as Class<UIComponent>]


    class Builder {
        private val components: MutableList<UIComponent> = ArrayList()

        fun with(component: UIComponent): Builder {
            components.add(component)
            return this
        }

        fun addAll(components: List<UIComponent>?): Builder {
            if (components != null) this.components.addAll(components)
            return this
        }

        fun build(): UIComponentHolder {
            return UIComponentHolder(components)
        }
    }
}