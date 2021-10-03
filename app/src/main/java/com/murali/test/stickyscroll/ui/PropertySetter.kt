package com.murali.test.stickyscroll.ui

import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat

object PropertySetter {
    @JvmStatic
    fun setTranslationZ(view: View, translationZ: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTranslationZ(view, translationZ)
        } else if (translationZ != 0f) {
            view.bringToFront()
            if (view.parent != null) {
                (view.parent as View).invalidate()
            }
        }
    }
}
