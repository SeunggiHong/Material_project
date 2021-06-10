package com.example.material_project.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.material_project.R
import com.example.material_project.model.SearchData

class SearchHistRecyclerViewAdapter(searchHistInterface: SearchHistInterface): RecyclerView.Adapter<SearchItemViewHolder>(){
    private var searchHistList:ArrayList<SearchData> = ArrayList()
    private var searchHistInterface : SearchHistInterface? = null

    init {
        this.searchHistInterface = searchHistInterface
    }

    //뷰홀더와 레이아웃을 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val searchItemViewHolder = SearchItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false),
            this.searchHistInterface!!
        )
        return searchItemViewHolder
    }

    override fun getItemCount(): Int {
        return searchHistList.size
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val dataItem = this.searchHistList[position]
        holder.bindWithView(dataItem)
    }

    fun submitList(searchHistList: ArrayList<SearchData>){
        this.searchHistList = searchHistList
    }

}