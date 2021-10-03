package com.murali.test.stickyscroll.ui.interfaces

interface ScrollViewListener {
    fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)
    fun onScrollStopped(isStoped: Boolean)
}
