package com.murali.test.stickyscroll.provider.interfaces

import androidx.annotation.StyleableRes

interface ResourceProvider {
    fun getResourceId(@StyleableRes styleResId: Int): Int
    fun recycle()
}
