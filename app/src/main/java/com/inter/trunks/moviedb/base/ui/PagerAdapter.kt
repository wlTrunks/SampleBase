package com.inter.trunks.moviedb.base.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    constructor(fm: FragmentManager, pages: MutableList<Page>) : this(fm) {
        this.pages = pages
    }

    private var pages: MutableList<Page>? = null


    override fun getCount(): Int = pages?.size ?: 0

    fun setPages(pages: MutableList<Page>) {
        this.pages = pages
        notifyDataSetChanged()
    }

    fun deletePage(page: Int) {
        if (page < pages!!.size) {
            pages!!.removeAt(page)
            notifyDataSetChanged()
        }
    }

    override fun getItem(position: Int): Fragment {
        return pages!![position].fragment!!
    }

    class Page(
        @param:DrawableRes val iconResId: Int = -1,
        @param:StringRes val titleResId: Int = -1,
        var fragment: Fragment?
    )
}
