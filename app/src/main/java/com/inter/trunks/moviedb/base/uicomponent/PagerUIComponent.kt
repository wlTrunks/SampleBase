package com.inter.trunks.moviedb.base.uicomponent

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.inter.trunks.moviedb.R
import com.inter.trunks.moviedb.base.ui.PagerAdapter
import com.inter.trunks.presentation.base.component.UIComponent

/**
 * Компонет ViewPager R.id.pager и TabLayout R.id.slidingTabs
 */
class PagerUIComponent(manager: FragmentManager) : UIComponent() {
    private var tabLayout: TabLayout? = null
    private var pager: ViewPager? = null
    private val adapter: PagerAdapter
    private var pageList: MutableList<PagerAdapter.Page>? = null

    init {
        adapter = PagerAdapter(manager)
    }

    fun setBackground(context: Context?, @ColorRes res: Int) {
        if (context != null && tabLayout != null)
            tabLayout!!.setBackgroundColor(ContextCompat.getColor(context, res))
    }

    override fun onViewCreated(layout: View) {
        super.onViewCreated(layout)
        // tabLayout = layout.findViewById(R.id.slidingTabs)
        // pager = layout.findViewById(R.id.pager)
        if (pager == null) throw RuntimeException("Setup ViewPager on layout")
        pager!!.adapter = adapter
        if (tabLayout != null) {
            tabLayout!!.setupWithViewPager(pager)
            setTabLayoutTitleIcon()
        }
    }

    fun setPages(pageList: MutableList<PagerAdapter.Page>) {
        this.pageList = pageList
        adapter.setPages(pageList)
        setTabLayoutTitleIcon()
    }

    fun <T : Fragment> getPageFragment(frClass: Class<T>): T? {
        var fr: T? = null
        pageList?.forEach {
            if (frClass.isInstance(it.fragment)) {
                fr = it.fragment as T
            }
        }
        return fr
    }

    private fun setTabLayoutTitleIcon() {
        if (tabLayout != null) {
            pageList?.let {
                for (i in pageList!!.indices) {
                    val resIcon = pageList!![i].iconResId
                    val resTitle = pageList!![i].titleResId
                    val tab = tabLayout!!.getTabAt(i)
                    tab?.let {
                        if (resIcon != -1) {
                            tab.setIcon(pageList!![i].iconResId)
                        }
                        if (resTitle != -1) {
                            tab.setText(resTitle)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pager!!.adapter = null
        pager = null
        if (pageList != null) {
            for (page in pageList!!) {
                page.fragment = null
            }
            this.pageList!!.clear()
        }
    }
}
