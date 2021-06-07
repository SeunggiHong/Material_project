package com.example.material_project.retrofit

import android.util.Log
import com.example.material_project.utils.Constants
import com.example.material_project.utils.RESPONSE_TYPE
import com.example.material_project.utils.UNSPLASH_API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response

class Retrofit_Manager {
    companion object {
        val instance = Retrofit_Manager()
    }

    private val retrofitInterfcae: Retrofit_Interface? = Retrofit_Clint.getClient(UNSPLASH_API.BASE_URL)?.create(Retrofit_Interface::class.java)

    fun searchPhotos(searchTerm: String?, completion: (RESPONSE_TYPE, String) -> Unit) {
        val term = searchTerm?: ""

        val call: Call<JsonElement> = retrofitInterfcae?.searchPhoto(searchTerm = term) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(Constants.TAG, "Retrofit_Manager-onFailure-Throwable:  $t")
                completion(RESPONSE_TYPE.FAIL, t.toString())
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(Constants.TAG, "Retrofit_Manager-onResponse-response :  ${response.body()}")
                completion(RESPONSE_TYPE.SUCCESS, response.body().toString())
            }

        })
    }
}