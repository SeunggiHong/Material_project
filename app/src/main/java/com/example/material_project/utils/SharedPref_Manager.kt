package com.example.material_project.utils

import android.content.Context
import android.util.Log
import com.example.material_project.model.SearchData
import com.example.material_project.utils.Constants.TAG
import com.google.gson.Gson

object SharedPref_Manager {
    private const val SHARED_SHEARCH_HISTORY = "shared_search_history"
    private const val KEY_SEARCH_HISTORY = "key_search_history"

    // 검색 목록을 저장
    fun storeSearchHistoryList(searchHistoryList: MutableList<SearchData>){
        Log.d(TAG, "SharedPrefManager - storeSearchHistoryList() called")
        val searchHistoryListString : String = Gson().toJson(searchHistoryList)
        Log.d(TAG, "SharedPrefManager - searchHistoryListString : $searchHistoryListString")
        val shared = App.instance.getSharedPreferences(SHARED_SHEARCH_HISTORY, Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putString(KEY_SEARCH_HISTORY, searchHistoryListString)
        editor.apply()
    }

    // 검색 목록 가져오기
    fun getSearchHistoryList() : MutableList<SearchData> {
        val shared = App.instance.getSharedPreferences(SHARED_SHEARCH_HISTORY, Context.MODE_PRIVATE)
        val storedSearchHistoryListString = shared.getString(KEY_SEARCH_HISTORY, "")!!
        var storedSearchHistoryList = ArrayList<SearchData>()

        if (storedSearchHistoryListString.isNotEmpty()){
            storedSearchHistoryList = Gson().fromJson(storedSearchHistoryListString, Array<SearchData>::class.java).
            toMutableList() as ArrayList<SearchData>
        }

        return storedSearchHistoryList
    }
}