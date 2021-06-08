package com.example.material_project

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.material_project.model.Photo
import com.example.material_project.utils.Constants

class PhotoActivity: AppCompatActivity() {
    var photoList = ArrayList<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        Log.d(Constants.TAG, "PhotoActivity onCreate")

        val bundle = intent.getBundleExtra("array_bundle")
    }
}