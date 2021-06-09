package com.example.material_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.material_project.retrofit.Retrofit_Manager
import com.example.material_project.utils.Constants
import com.example.material_project.utils.RESPONSE_STATUS
import com.example.material_project.utils.SEARCH_TYPE
import com.example.material_project.utils.onMyTextChanged
import com.example.material_project.views.PhotoActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_button_search.*

class MainActivity : AppCompatActivity() {
    private var current_Search: SEARCH_TYPE = SEARCH_TYPE.PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(Constants.TAG, "MainActivity onCreate")

        rg_search.setOnCheckedChangeListener{ _, checkId ->
            when(checkId){
                R.id.rb_search1 -> {
                    Log.d(Constants.TAG, "photo search")
                    textField.hint = resources.getString(R.string.hint_rb1)
                    textField.startIconDrawable = resources.getDrawable(R.drawable.ic_baseline_insert_photo_24, resources.newTheme())
                    this.current_Search = SEARCH_TYPE.PHOTO
                }
                R.id.rb_search2 -> {
                    Log.d(Constants.TAG, "person search")
                    textField.hint = resources.getString(R.string.hint_rb2)
                    textField.startIconDrawable = resources.getDrawable(R.drawable.ic_baseline_person_24, resources.newTheme())
                    this.current_Search = SEARCH_TYPE.USER
                }
            }
            Log.d(Constants.TAG, "MainActivity - setOnCheckedChangeListener, current_Search = $current_Search")
        }

        et_search.onMyTextChanged {
            if(it.toString().count() > 0) {
                fl_search.visibility = View.VISIBLE
                sc_view.scrollTo(0, 200)
                textField.helperText = " "
            } else {
                fl_search.visibility = View.INVISIBLE
                textField.helperText = resources.getString(R.string.hint_helper)
            }
            if (it.toString().count() == 12) {
                Log.e(Constants.TAG, "edit text error")
                Toast.makeText(this, "검색어는 12자 까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        btn_search.setOnClickListener {
            Log.d(Constants.TAG, "btn_search clicked current_Search :  $current_Search")
            val userSearchInput = et_search.text.toString()
            this.handleSearchButton()

            Retrofit_Manager.instance.searchPhotos(searchTerm = et_search.text.toString(), completion = {
                responseType, responseDataArrayList ->
                when(responseType) {
                    RESPONSE_STATUS.SUCCESS -> {
                        Log.d(Constants.TAG, "api call success : ${responseDataArrayList?.size}")
                        val intent = Intent(this, PhotoActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("photo_array_list", responseDataArrayList)
                        intent.putExtra("array_bundle", bundle)
                        intent.putExtra("search_term", userSearchInput)
                        startActivity(intent)
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Toast.makeText(this, "Unsplsh api error", Toast.LENGTH_SHORT).show()
                        Log.d(Constants.TAG, "api call fail : $responseDataArrayList")
                    }
                    RESPONSE_STATUS.NO_CONTENT -> {
                        Toast.makeText(this, "No results were found for your search.", Toast.LENGTH_SHORT).show()
                    }
                }
                btn_search.visibility = View.VISIBLE
                btn_progress.visibility = View.INVISIBLE
                btn_search.text = resources.getString(R.string.btn_search)
                et_search.setText("")
            })

        }

    }

    private fun handleSearchButton() {
        btn_progress.visibility = View.VISIBLE
        btn_search.visibility = View.INVISIBLE
    }

}