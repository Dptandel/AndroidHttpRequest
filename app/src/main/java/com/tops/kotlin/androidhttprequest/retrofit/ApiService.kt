package com.tops.kotlin.androidhttprequest.retrofit

import com.tops.kotlin.androidhttprequest.models.City
import com.tops.kotlin.androidhttprequest.models.Country
import com.tops.kotlin.androidhttprequest.models.State
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("countries")
    fun getCountries(@Header("Authorization") token: String): Call<MutableList<Country>>

    @GET("states/{country_name}")
    fun getStates(
        @Header("Authorization") token: String,
        @Path("country_name") countryName: String
    ): Call<MutableList<State>>

    @GET("cities/{state_name}")
    fun getCities(
        @Header("Authorization") token: String,
        @Path("state_name") stateName: String
    ): Call<MutableList<City>>
}