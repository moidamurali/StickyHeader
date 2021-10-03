package com.murali.test.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.murali.test.R
import com.murali.test.utils.Common

class WidgetsListAdapter(
    private val widgetButtonClickCallback: ((Int) -> Unit)? = null,
    private val dataSet: List<String>,
    private val selectedPageNumber: Int,
    private val context: Context
) : RecyclerView.Adapter<WidgetsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pagesButton: Button = view.findViewById(R.id.page_btn)
        val arrowView: ImageView = view.findViewById(R.id.arrow_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pagesButton.text = dataSet[position]
        if (dataSet.size == position + 1) {
            holder.arrowView.visibility = View.GONE
        }

        if (selectedPageNumber == position) {
            Common.currentButtonStyle(context, holder.pagesButton)
            holder.arrowView.setImageDrawable(context.getDrawable(R.drawable.ic_forward_selected))
        } else if (position <selectedPageNumber) {
            Common.visitedButtonStyle(context, holder.pagesButton)
        }

        // Define click listener for the ViewHolder's View.
        holder.pagesButton.setOnClickListener {
            widgetButtonClickCallback?.invoke(position)
        }
    }

    override fun getItemCount() = dataSet.size
}
