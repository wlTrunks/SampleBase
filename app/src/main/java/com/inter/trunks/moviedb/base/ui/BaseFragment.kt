package com.inter.trunks.moviedb.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inter.trunks.presentation.base.component.UIComponent
import com.inter.trunks.presentation.base.component.UIComponentHolder

abstract class BaseFragment : Fragment(), LayoutResources {

    protected var componentHolder: UIComponentHolder? = null

    open fun getComponentUIList(): List<UIComponent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponentUIList()?.let { componentHolder = createHolder(getComponentUIList()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        componentHolder?.onViewCreated(context!!, view)
    }

    private fun createHolder(components: List<UIComponent>?): UIComponentHolder =
        UIComponentHolder.Builder().addAll(components).build()

    override fun onDestroyView() {
        super.onDestroyView()
        componentHolder?.onDestroyView()
    }

    override fun onDestroy() {
        componentHolder?.onDestroy()
        componentHolder = null
        super.onDestroy()
    }
}