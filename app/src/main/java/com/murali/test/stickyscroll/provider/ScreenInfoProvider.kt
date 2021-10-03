package com.murali.test.stickyscroll.provider

import android.content.Context
import android.graphics.Point
import com.murali.test.stickyscroll.provider.interfaces.ScreenInfoProvider

class ScreenInfoProvider(private val mContext: Context) :
    ScreenInfoProvider {
    override val screenHeight: Int
        get() = deviceDimension.y
    override val screenWidth: Int
        get() = deviceDimension.x
    val deviceDimension: Point
        get() {
            val lPoint = Point()
            val metrics = mContext.resources.displayMetrics
            lPoint.x = metrics.widthPixels
            lPoint.y = metrics.heightPixels
            return lPoint
        }
}
