package com.example.material_project.retrofit

import android.util.Log
import com.example.material_project.model.Photo
import com.example.material_project.utils.Constants
import com.example.material_project.utils.RESPONSE_STATUS
import com.example.material_project.utils.UNSPLASH_API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat

class Retrofit_Manager {
    companion object {
        val instance = Retrofit_Manager()
    }

    private val retrofitInterfcae: Retrofit_Interface? = Retrofit_Clint.getClient(UNSPLASH_API.BASE_URL)?.create(Retrofit_Interface::class.java)

    fun searchPhotos(searchTerm: String?, completion: (RESPONSE_STATUS, ArrayList<Photo>?) -> Unit) {
        val term = searchTerm?: ""

        val call: Call<JsonElement> = retrofitInterfcae?.searchPhoto(searchTerm = term) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(Constants.TAG, "Retrofit_Manager-onFailure-Throwable:  $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(Constants.TAG, "Retrofit_Manager-onResponse-response :  ${response.body()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedPhotoDateArray = ArrayList<Photo>()
                            val body = it.asJsonObject
                            val results = body.getAsJsonArray("results")
                            val total = body.get("total").asInt

                            if (total == 0) {
                                completion(RESPONSE_STATUS.NO_CONTENT, null)
                            } else {
                                results.forEach {resultItem ->
                                    val resultItemObject = resultItem.asJsonObject
                                    val user = resultItemObject.get("user").asJsonObject
                                    val userName: String = user.get("username").asString
                                    val likeCount: Int = resultItemObject.get("likes").asInt
                                    val thumnailLink = resultItemObject.get("urls").asJsonObject.get("thumb").asString
                                    val createdAt = resultItemObject.get("created_at").asString

                                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    val formatter = SimpleDateFormat("yyyy년\nMM월 dd일")
                                    val outputDateString = formatter.format(parser.parse(createdAt))

                                    val photoItem = Photo(
                                        author     = userName,
                                        likesCount = likeCount,
                                        thumbnail  = thumnailLink,
                                        createdAt  = outputDateString
                                    )
                                    parsedPhotoDateArray.add(photoItem)
                                }
                                completion(RESPONSE_STATUS.SUCCESS, parsedPhotoDateArray)
                            }
                        }
                    }
                }
            }

        })
    }
}