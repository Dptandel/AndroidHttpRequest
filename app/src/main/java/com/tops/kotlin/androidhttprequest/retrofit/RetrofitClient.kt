package com.tops.kotlin.androidhttprequest.retrofit

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun getService(context: Context): ApiService {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://www.universal-tutorial.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}