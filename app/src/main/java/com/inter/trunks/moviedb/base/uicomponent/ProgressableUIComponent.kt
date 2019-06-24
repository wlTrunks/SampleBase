package com.inter.trunks.moviedb.base.uicomponent

import android.graphics.PorterDuff
import android.widget.ProgressBar
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.inter.trunks.moviedb.R
import com.inter.trunks.presentation.base.component.Progressable
import com.inter.trunks.presentation.base.component.UIComponent


class ProgressableUIComponent : UIComponent(),
    Progressable {
    lateinit var progressView: View
        private set
    private var rootView: View? = null
    var needHideRootView = true
    var animateRootView = false
    private var progressViewColor = R.color.colorAccent

    private val progressColor: Int
        get() {
            var result = 0
            try {
                result = context!!.resources.getColor(progressViewColor)
            } catch (ignored: Exception) {
            }
            return result
        }

    override fun onViewCreated(layout: View) {
        super.onViewCreated(layout)
//        progressView = layout.findViewById(R.id.progress)
        rootView = layout.findViewById(R.id.root)
        if (progressView is ProgressBar) {
            (progressView as ProgressBar).indeterminateDrawable
                .setColorFilter(progressColor, PorterDuff.Mode.SRC_IN)
        }

    }

    override fun begin() {
        progressView.visibility = View.VISIBLE
        if (needHideRootView) rootView?.visibility = View.GONE
    }

    override fun end() {
        progressView.visibility = View.GONE
        rootView?.visibility = View.VISIBLE
        animateRootView()
    }

    private fun animateRootView() {
        if (animateRootView) {
            val animation = getAnimationForContent()
            rootView?.startAnimation(animation)
        }
    }

    fun setProgressViewColor(progressViewColor: Int): ProgressableUIComponent {
        this.progressViewColor = progressViewColor
        return this
    }

    fun setNeedHideRootView(needHideRootView: Boolean): ProgressableUIComponent {
        this.needHideRootView = needHideRootView
        return this
    }

    protected fun getAnimationForContent(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.fadein)
    }

}