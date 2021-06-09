package com.example.material_project.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.material_project.R
import com.example.material_project.model.Photo
import com.example.material_project.recyclerview.PhotoGridRecyclerViewAdapter
import com.example.material_project.utils.Constants
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity: AppCompatActivity() {
    private var photoList = ArrayList<Photo>()
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        Log.d(Constants.TAG, "PhotoActivity onCreate")

        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")

        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<Photo>

        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        this.photoGridRecyclerViewAdapter.submitlist(photoList)
        rc_view.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rc_view.adapter = this.photoGridRecyclerViewAdapter

    }
}