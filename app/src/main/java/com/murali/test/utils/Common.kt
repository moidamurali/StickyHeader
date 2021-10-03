package com.murali.test.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.murali.test.R

class Common {

    companion object {

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

        fun visitedButtonStyle(context: Context, button: Button) {
            button.let {
                it.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
        }

        fun currentButtonStyle(context: Context, button: Button) {
            button.let {
                it.setBackgroundColor(ContextCompat.getColor(context, R.color.current_button_color))
            }
        }
    }
}
