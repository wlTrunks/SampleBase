package com.inter.trunks.presentation.base.component


interface ComponentHolder {
    fun <T : UIComponent> getComponent(componentClass: Class<T>): T?
}