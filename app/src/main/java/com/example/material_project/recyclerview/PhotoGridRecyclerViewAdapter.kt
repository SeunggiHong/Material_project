package com.example.material_project.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.material_project.R
import com.example.material_project.model.PhotoData

class PhotoGridRecyclerViewAdapter: RecyclerView.Adapter<PhotoItemViewHolder>() {
    private var photoList = ArrayList<PhotoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        val photoItemViewHolder = PhotoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))
        return photoItemViewHolder
    }

    override fun getItemCount(): Int {
        return this.photoList.size
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        holder.bindWithView(this.photoList[position])
    }

    fun submitList(photoList: ArrayList<PhotoData>) {
        this.photoList = photoList
    }

}