package com.murali.test.stickyscroll.ui

import androidx.annotation.StyleableRes
import com.murali.test.stickyscroll.provider.interfaces.ResourceProvider
import com.murali.test.stickyscroll.provider.interfaces.ScreenInfoProvider
import com.murali.test.stickyscroll.ui.interfaces.StickyScrollPresentation

class StickyScrollPresenter(
    stickyScrollPresentation: StickyScrollPresentation,
    screenInfoProvider: ScreenInfoProvider,
    typedArrayResourceProvider: ResourceProvider
) {
    private val typedArrayResourceProvider: ResourceProvider
    private val mStickyScrollPresentation: StickyScrollPresentation
    private val deviceHeight: Int
    private var stickFooterHeight = 0
    private var stickFooterInitialTranslation = 0
    private var stickFooterInitialLocation = 0
    private var stickHeaderInitialLocation = 0
    var isFooterSticky = false
        private set
    var isHeaderSticky = false
        private set
    @JvmField
    var isScrolled = false
    fun onGlobalLayoutChange(@StyleableRes headerRes: Int, @StyleableRes footerRes: Int) {
        val headerId = typedArrayResourceProvider.getResourceId(headerRes)
        if (headerId != 0) {
            mStickyScrollPresentation.initHeaderView(headerId)
        }
        val footerId = typedArrayResourceProvider.getResourceId(footerRes)
        if (footerId != 0) {
            mStickyScrollPresentation.initFooterView(footerId)
        }
        typedArrayResourceProvider.recycle()
    }

    fun initStickyFooter(measuredHeight: Int, initialStickyFooterLocation: Int) {
        stickFooterHeight = measuredHeight
        stickFooterInitialLocation = initialStickyFooterLocation
        stickFooterInitialTranslation =
            deviceHeight - initialStickyFooterLocation - stickFooterHeight
        if (stickFooterInitialLocation > deviceHeight - stickFooterHeight) {
            mStickyScrollPresentation.stickFooter(stickFooterInitialTranslation)
            isFooterSticky = true
        }
    }

    fun initStickyHeader(headerTop: Int) {
        stickHeaderInitialLocation = headerTop
    }

    fun onScroll(scrollY: Int) {
        isScrolled = true
        handleFooterStickiness(scrollY)
        handleHeaderStickiness(scrollY)
    }

    private fun handleFooterStickiness(scrollY: Int) {
        if (scrollY > stickFooterInitialLocation - deviceHeight + stickFooterHeight) {
            mStickyScrollPresentation.freeFooter()
            isFooterSticky = false
        } else {
            mStickyScrollPresentation.stickFooter(stickFooterInitialTranslation + scrollY)
            isFooterSticky = true
        }
    }

    private fun handleHeaderStickiness(scrollY: Int) {
        if (scrollY > stickHeaderInitialLocation) {
            mStickyScrollPresentation.stickHeader(scrollY - stickHeaderInitialLocation)
            isHeaderSticky = true
        } else {
            mStickyScrollPresentation.freeHeader()
            isHeaderSticky = false
        }
    }

    fun recomputeFooterLocation(footerTop: Int) {
        if (isScrolled) {
            stickFooterInitialTranslation = deviceHeight - footerTop - stickFooterHeight
            stickFooterInitialLocation = footerTop
        } else {
            initStickyFooter(stickFooterHeight, footerTop)
        }
        handleFooterStickiness(mStickyScrollPresentation.currentScrollYPos)
    }

    fun recomputeHeaderLocation(headerTop: Int) {
        initStickyHeader(headerTop)
        handleHeaderStickiness(mStickyScrollPresentation.currentScrollYPos)
    }

    init {
        deviceHeight = screenInfoProvider.screenHeight
        this.typedArrayResourceProvider = typedArrayResourceProvider
        mStickyScrollPresentation = stickyScrollPresentation
    }
}
