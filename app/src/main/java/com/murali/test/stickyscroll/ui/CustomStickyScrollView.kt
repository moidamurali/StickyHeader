package com.murali.test.stickyscroll.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ScrollView
import com.murali.test.R
import com.murali.test.stickyscroll.provider.ResourceProvider
import com.murali.test.stickyscroll.provider.ScreenInfoProvider
import com.murali.test.stickyscroll.ui.PropertySetter.setTranslationZ
import com.murali.test.stickyscroll.ui.interfaces.ScrollViewListener
import com.murali.test.stickyscroll.ui.interfaces.StickyScrollPresentation

class CustomStickyScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ScrollView(context, attrs, defStyle), StickyScrollPresentation {
    var scrollViewListener: ScrollViewListener? = null
    private var stickFooterView: View? = null
    private var stickHeaderView: View? = null
    private val stickyScrollPresenter: StickyScrollPresenter
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!changed) {
            stickyScrollPresenter.recomputeFooterLocation(getRelativeTop(stickFooterView))
        }
        stickHeaderView?.top?.let { stickyScrollPresenter.recomputeHeaderLocation(it) }
    }

    private fun getRelativeTop(myView: View?): Int {
        return if (myView == null) {
            0
        } else {
            if (myView.parent === myView.rootView) {
                myView.top
            } else {
                myView.top + getRelativeTop(myView.parent as View)
            }
        }
    }

    override fun initHeaderView(id: Int) {
        stickHeaderView = findViewById(id)
        stickHeaderView?.getTop()?.let { stickyScrollPresenter.initStickyHeader(it) }
    }

    override fun initFooterView(id: Int) {
        stickFooterView = findViewById(id)
        stickyScrollPresenter.initStickyFooter(
            stickFooterView!!.getMeasuredHeight(),
            getRelativeTop(stickFooterView)
        )
    }

    override fun freeHeader() {
        stickHeaderView?.translationY = 0f
        stickHeaderView?.let { setTranslationZ(it, 0f) }
    }

    override fun freeFooter() {
        stickFooterView?.translationY = 0f
    }

    override fun stickHeader(translationY: Int) {
        stickHeaderView?.translationY = translationY.toFloat()
        stickHeaderView?.let { setTranslationZ(it, 1f) }
    }

    override fun stickFooter(translationY: Int) {
        stickFooterView?.translationY = translationY.toFloat()
    }

    override val currentScrollYPos: Int
        get() = scrollY

    override fun onScrollChanged(mScrollX: Int, mScrollY: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(mScrollX, mScrollY, oldX, oldY)
        stickyScrollPresenter.onScroll(mScrollY)
        if (scrollViewListener != null) {
            scrollViewListener!!.onScrollChanged(mScrollX, mScrollY, oldX, oldY)
        }
    }

    val isFooterSticky: Boolean
        get() = stickyScrollPresenter.isFooterSticky
    val isHeaderSticky: Boolean
        get() = stickyScrollPresenter.isHeaderSticky

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        if (scrollViewListener != null) {
            scrollViewListener!!.onScrollStopped(clampedY)
        }
    }

    public override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_STATE, super.onSaveInstanceState())
        bundle.putBoolean(SCROLL_STATE, stickyScrollPresenter.isScrolled)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        var state: Parcelable? = state
        if (state is Bundle) {
            val bundle = state
            stickyScrollPresenter.isScrolled = bundle.getBoolean(SCROLL_STATE)
            state = bundle.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(state)
    }

    companion object {
        private const val SCROLL_STATE = "SCROLL_STATE"
        private const val SUPER_STATE = "SUPER_STATE"
    }

    init {
        val screenInfoProvider = ScreenInfoProvider(
            context!!
        )
        val resourceProvider = ResourceProvider(
            context, attrs, R.styleable.StickyScrollView
        )
        stickyScrollPresenter = StickyScrollPresenter(this, screenInfoProvider, resourceProvider)
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                stickyScrollPresenter.onGlobalLayoutChange(
                    R.styleable.StickyScrollView_stickyHeader,
                    R.styleable.StickyScrollView_stickyFooter
                )
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}
