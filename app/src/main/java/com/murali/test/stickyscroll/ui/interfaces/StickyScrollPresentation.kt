package com.murali.test.stickyscroll.ui.interfaces

interface StickyScrollPresentation {
    fun freeHeader()
    fun freeFooter()
    fun stickHeader(translationY: Int)
    fun stickFooter(translationY: Int)
    fun initHeaderView(id: Int)
    fun initFooterView(id: Int)
    val currentScrollYPos: Int
}
