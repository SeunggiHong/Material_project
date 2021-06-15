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
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.material_project.R
import com.example.material_project.model.PhotoData
import com.example.material_project.model.SearchData
import com.example.material_project.recyclerview.PhotoGridRecyclerViewAdapter
import com.example.material_project.recyclerview.SearchHistInterface
import com.example.material_project.recyclerview.SearchHistRecyclerViewAdapter
import com.example.material_project.retrofit.Retrofit_Manager
import com.example.material_project.utils.Constants
import com.example.material_project.utils.Constants.TAG
import com.example.material_project.utils.RESPONSE_STATUS
import com.example.material_project.utils.SharedPref_Manager
import com.example.material_project.utils.toSimpleString
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PhotoActivity: AppCompatActivity(), SearchView.OnQueryTextListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, SearchHistInterface {
    private var photoList = ArrayList<PhotoData>()
    private var searchHistoryList = ArrayList<SearchData>()
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter
    private lateinit var searchHistRecyclerViewAdapter: SearchHistRecyclerViewAdapter
    private lateinit var mSearchView: SearchView
    private lateinit var mSearchEditText: EditText

    //옵저버 제거를 위한
    private var mCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        Log.d(Constants.TAG, "PhotoActivity onCreate")

        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")
        topAppBar.title = searchTerm
        setSupportActionBar(topAppBar)

        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<PhotoData>
        this.setPhotohRecyclerView(this.photoList)

        this.searchHistoryList = SharedPref_Manager.getSearchHistoryList() as ArrayList<SearchData>
        this.searchHistoryList.forEach{
            Log.d(TAG, "저장된 검색 기록 : ${it.term}, ${it.timestamp}")
        }
        this.setSearchRecyclerView(this.searchHistoryList)

        sw_search.isChecked = SharedPref_Manager.checkSearchHistoryMode()

        if(searchTerm!!.isNotEmpty()) {
            val term = searchTerm?.let{
                it
            }?: ""
            this.filterSearchHist(term)
        }

        handleSearchViewUi()
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    private fun setPhotohRecyclerView(PhotoList: ArrayList<PhotoData>) {
        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        this.photoGridRecyclerViewAdapter.submitList(PhotoList)
        //역순 정렬
        val photoGridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        rc_view.apply {
            layoutManager = photoGridLayoutManager
            adapter = photoGridRecyclerViewAdapter
        }
    }

    private fun setSearchRecyclerView(searchHistoryList: ArrayList<SearchData>) {
        this.searchHistRecyclerViewAdapter = SearchHistRecyclerViewAdapter(this)
        this.searchHistRecyclerViewAdapter.submitList(searchHistoryList)
        //역순 정렬
        val seachLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true)
        seachLinearLayoutManager.stackFromEnd = true
        rc_search_view.apply {
            layoutManager = seachLinearLayoutManager
            this.scrollToPosition(searchHistRecyclerViewAdapter.itemCount - 1)
            adapter = searchHistRecyclerViewAdapter
        }
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
//                        li_view.visibility = View.VISIBLE
                        handleSearchViewUi()
                    }
                    false -> {
                        Log.d(TAG, "onCreateOptionsMenu: $hasExpanded")
                        li_view.visibility = View.INVISIBLE
                    }
                }
            }
            mSearchEditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
            val editTextChangeObserver = mSearchEditText.textChanges()
            val editTextSubscribtion : Disposable = editTextChangeObserver.debounce(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io()).subscribeBy(
                    onNext = {
                        Log.d("RX2011", "onCreateOptionsMenu - onNext : $it")
                        if (it.isNotEmpty()) {
                            searchPhotApiCall(it.toString())
                        }
                    },
                    onComplete = {
                        Log.d("RX2011", "onCreateOptionsMenu - onComplete")
                    },
                    onError = {
                        Log.d("RX2011", "onCreateOptionsMenu -  onError : $it")
                    }
                )

            mCompositeDisposable.add(editTextSubscribtion)

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
            this.filterSearchHist(query)
            this.searchPhotApiCall(query)
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

        if (userInputText.length in 1..12) {
            searchPhotApiCall(userInputText)
        }

        return true
    }

    override fun onCheckedChanged(switch: CompoundButton?, isChecked: Boolean) {
        when(switch) {
            sw_search -> {
                if(isChecked) {
                    Log.d(TAG, "onCheckedChanged: $isChecked")
                    SharedPref_Manager.setSearchHistoryMode(isActive = true)
                } else {
                    Log.d(TAG, "onCheckedChanged: $isChecked")
                    SharedPref_Manager.setSearchHistoryMode(isActive = false)
                }
            }
        }

    }

    override fun onClick(view: View?) {
        when(view) {
            btn_delete -> {
                SharedPref_Manager.clearAllHistoryList()
                this.searchHistoryList.clear()

                handleSearchViewUi()
            }
        }
    }

    override fun onSearchItemDeleteClicked(position: Int) {
        Log.d(TAG, "onSearchItemDeleteClicked called position : $position" )
        this.searchHistoryList.removeAt(position)
        SharedPref_Manager.storeSearchHistoryList(this.searchHistoryList)
        this.searchHistRecyclerViewAdapter.notifyDataSetChanged()
        handleSearchViewUi()
    }

    override fun onSearchItemClicked(position: Int) {
        Log.d(TAG, "onSearchItemClicked called" )
        val queryString = this.searchHistoryList[position].term
        searchPhotApiCall(queryString)
        topAppBar.title = queryString
        this.filterSearchHist(searchTerm = queryString)
        this.topAppBar.collapseActionView()
    }

    private fun searchPhotApiCall(query: String) {
        Retrofit_Manager.instance.searchPhotos(searchTerm = query,completion = {status, list ->
            when(status) {
                RESPONSE_STATUS.SUCCESS -> {
                    Log.d(TAG, "searchPhotApiCall: SUCCESS")
                    if (list != null) {
                        this.photoList.clear()
                        this.photoList = list
                        this.photoGridRecyclerViewAdapter.submitList(this.photoList)
                        this.photoGridRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }
                RESPONSE_STATUS.NO_CONTENT -> {
                    Toast.makeText(this, "No results were found for your search.", Toast.LENGTH_SHORT).show()
                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d(TAG, "searchPhotApiCall: FAIL")
                }
            }
        })
    }

    private fun handleSearchViewUi() {
        if(this.searchHistoryList.size > 0){
            rc_search_view.visibility = View.VISIBLE
            tv_recent_search.visibility = View.VISIBLE
            btn_delete.visibility = View.VISIBLE
        } else {
            rc_search_view.visibility = View.INVISIBLE
            tv_recent_search.visibility = View.INVISIBLE
            btn_delete.visibility = View.INVISIBLE
        }
    }

    private fun filterSearchHist(searchTerm : String){
        if(SharedPref_Manager.checkSearchHistoryMode()) {
            var removeArray = ArrayList<Int>()
            this.searchHistoryList.forEachIndexed {index, searchDataItem ->
                if (searchDataItem.term == searchTerm) {
                    removeArray.add(index)
                }
            }
            removeArray.forEach {
                this.searchHistoryList.removeAt(it)
            }

            var searchData = SearchData(term = searchTerm, timestamp = Date().toSimpleString())
            this.searchHistoryList.add(searchData)
            SharedPref_Manager.storeSearchHistoryList(this.searchHistoryList)

            this.searchHistRecyclerViewAdapter.notifyDataSetChanged()
        }
    }
}