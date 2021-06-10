package com.example.material_project.recyclerview

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.material_project.model.SearchData
import com.example.material_project.utils.Constants.TAG
import kotlinx.android.synthetic.main.search_item.view.*

class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val searchTermText = itemView.tv_term_name
    private val searchDateText = itemView.tv_date
    private val deleteSearchBtn = itemView.iv_delete
    private val clSearchView = itemView.cl_search_view

    init {
        deleteSearchBtn.setOnClickListener(this)
        clSearchView.setOnClickListener(this)
    }

    fun bindWithView(searchItem: SearchData) {
        searchTermText.text = searchItem.term
        searchDateText.text = searchItem.timestamp
    }

    override fun onClick(view: View?) {
        when (view)  {
            deleteSearchBtn -> {
                Log.d(TAG, "SearchItemViewHolder onClick - deleteSearchBtn")
            }
            clSearchView -> {
                Log.d(TAG, "SearchItemViewHolder onClick - clView")
            }
        }
    }


}