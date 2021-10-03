package com.murali.test.stickyscroll.provider

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import com.murali.test.stickyscroll.provider.interfaces.ResourceProvider

class ResourceProvider(context: Context, attrs: AttributeSet?, @StyleableRes styleRes: IntArray?) :
    ResourceProvider {
    private val mTypeArray: TypedArray = context.obtainStyledAttributes(attrs, styleRes!!)
    override fun getResourceId(@StyleableRes styleResId: Int): Int {
        return mTypeArray.getResourceId(styleResId, 0)
    }

    override fun recycle() {
        mTypeArray.recycle()
    }
}
