package com.example.material_project.retrofit

import android.util.Log
import com.example.material_project.utils.Constants
import com.example.material_project.utils.UNSPLASH_API
import com.example.material_project.utils.isJsonArray
import com.example.material_project.utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

object Retrofit_Clint {
    private var retrofitClient: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {

        val client = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.d(Constants.TAG, "loggingInterceptor-message : $message")

                when {
                    message.isJsonObject() -> Log.d(Constants.TAG, JSONObject(message).toString(4))
                    message.isJsonArray() -> Log.d(Constants.TAG, JSONObject(message).toString(4))
                    else -> {
                        try {
                            Log.d(Constants.TAG, JSONObject(message).toString(4))
                        } catch(e: Exception) {
                            Log.d(Constants.TAG, message)
                        }
                    }
                }
            }
        })
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(loggingInterceptor)

        val baseParameterInterceptor: Interceptor = (object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val addUrl = originalRequest.url.newBuilder().addQueryParameter("client_id", UNSPLASH_API.CLIENT_ID).build()
                val finalRequest = originalRequest.newBuilder()
                    .url(addUrl)
                    .method(originalRequest.method, originalRequest.body)
                    .build()

                return chain.proceed(finalRequest)
            }
        })
        client.addInterceptor(baseParameterInterceptor)
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(10, TimeUnit.SECONDS)
        client.writeTimeout(10, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)

        if (retrofitClient == null) {
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
        return retrofitClient
    }

}