package com.example.material_project.retrofit

import com.example.material_project.utils.UNSPLASH_API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Retrofit_Interface {

    @GET(UNSPLASH_API.SEARCH_PHOTO)
    fun searchPhoto(@Query("query") searchTerm: String) : Call<JsonElement>

    @GET(UNSPLASH_API.SEARCH_USERS)
    fun searchUser(@Query("query") searchTerm: String) : Call<JsonElement>

}