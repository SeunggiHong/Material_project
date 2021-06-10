package com.example.material_project.recyclerview

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.material_project.model.SearchData
import com.example.material_project.utils.Constants.TAG
import kotlinx.android.synthetic.main.search_item.view.*

class SearchItemViewHolder(itemView: View, searchHistInterface: SearchHistInterface) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val searchTermText = itemView.tv_term_name
    private val searchDateText = itemView.tv_date
    private val deleteSearchBtn = itemView.iv_delete
    private val clSearchView = itemView.cl_search_view
    private var searchHistInterface : SearchHistInterface

    init {
        deleteSearchBtn.setOnClickListener(this)
        clSearchView.setOnClickListener(this)
        this.searchHistInterface = searchHistInterface
    }

    fun bindWithView(searchItem: SearchData) {
        searchTermText.text = searchItem.term
        searchDateText.text = searchItem.timestamp
    }

    override fun onClick(view: View?) {
        when (view)  {
            deleteSearchBtn -> {
                Log.d(TAG, "SearchItemViewHolder onClick - deleteSearchBtn")
                this.searchHistInterface.onSearchItemDeleteClicked(adapterPosition)
            }
            clSearchView -> {
                Log.d(TAG, "SearchItemViewHolder onClick - clView")
                this.searchHistInterface.onSearchItemClicked(adapterPosition)
            }
        }
    }


}