package com.murali.test.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.murali.test.adapter.WidgetsListAdapter
import com.murali.test.databinding.PagesButtonWidgetBinding

class PagesButtonWidget(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var _binding: PagesButtonWidgetBinding? = null
    private val binding: PagesButtonWidgetBinding get() = _binding!!
    private lateinit var widgetsListAdapter: WidgetsListAdapter

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        _binding = PagesButtonWidgetBinding.inflate(layoutInflater, this)
    }

    fun bindWidgetAdapter(adapter: WidgetsListAdapter) {
        widgetsListAdapter = adapter
        binding.pagesRecyclerview.adapter = widgetsListAdapter
    }
}
