package com.vtvcab.on.aosnews.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(ApiUtil.HOME_URL)
    fun getUrl(@Query("name") name : String) : Call<String>
}