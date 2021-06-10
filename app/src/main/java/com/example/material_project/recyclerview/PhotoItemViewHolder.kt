package com.example.material_project.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.material_project.utils.App
import com.example.material_project.R
import com.example.material_project.model.PhotoData
import kotlinx.android.synthetic.main.photo_item.view.*

class PhotoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val photoImageView = itemView.iv_photo
    private val photoCreatedAtText = itemView.tv_date
    private val photoLikesCountText = itemView.tv_likes

    fun bindWithView(photoItem: PhotoData) {
        photoCreatedAtText.text = photoItem.createdAt
        photoLikesCountText.text = photoItem.likesCount.toString()
        Glide.with(App.instance).load(photoItem.thumbnail).placeholder(R.drawable.ic_baseline_insert_photo_24).into(photoImageView)
    }

}