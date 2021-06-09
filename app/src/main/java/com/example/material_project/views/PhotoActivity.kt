package com.example.material_project.views

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SearchView
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.material_project.R
import com.example.material_project.model.Photo
import com.example.material_project.recyclerview.PhotoGridRecyclerViewAdapter
import com.example.material_project.utils.Constants
import com.example.material_project.utils.Constants.TAG
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity: AppCompatActivity(), SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private var photoList = ArrayList<Photo>()
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter
    private lateinit var mSearchView: SearchView
    private lateinit var mSearchEditText: EditText

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

        topAppBar.title = searchTerm
        setSupportActionBar(topAppBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(Constants.TAG, "onCreateOptionsMenu called")

        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        this.mSearchView = menu?.findItem(R.id.search_menu)?.actionView as SearchView
        this.mSearchView.apply{
            this.queryHint = resources.getString(R.string.hint_helper)
            this.setOnQueryTextListener(this@PhotoActivity)
            this.setOnQueryTextFocusChangeListener{ _, hasExpanded ->
                when(hasExpanded) {
                    true -> {
                        Log.d(TAG, "onCreateOptionsMenu: $hasExpanded")
                        li_view.visibility = View.VISIBLE
                    }
                    false -> {
                        Log.d(TAG, "onCreateOptionsMenu: $hasExpanded")
                        li_view.visibility = View.INVISIBLE
                    }
                }
            }
            mSearchEditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
        }
        this.mSearchEditText.apply {
            this.filters = arrayOf(InputFilter.LengthFilter(12))
            this.setTextColor(Color.parseColor("#ffffff"))
            this.setHintTextColor(Color.parseColor("#ffffff"))
        }
        this.sw_search.setOnCheckedChangeListener(this)
        this.btn_delete.setOnClickListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "onQueryTextSubmit: query : ${query}")
        if(!query.isNullOrEmpty()) {
            this.topAppBar.title = query

        }

        this.topAppBar.collapseActionView()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange: newText :${newText}")
        val userInputText = newText ?: ""
        if (userInputText.count() == 12){
            Toast.makeText(this, R.string.search_view_edittext_msg,Toast.LENGTH_SHORT).show()
        }


        return true
    }

    override fun onCheckedChanged(switch: CompoundButton?, isChecked: Boolean) {
        when(switch) {
            sw_search -> {
                if(isChecked) {
                    Log.d(TAG, "onCheckedChanged: $isChecked")
                } else {
                    Log.d(TAG, "onCheckedChanged: $isChecked")
                }
            }
        }

    }

    override fun onClick(view: View?) {
        when(view) {
            btn_delete -> {
                Log.d(TAG, "onClick: btn_delete")
            }
        }
    }

}