package com.app.apilogui

import com.google.gson.JsonObject
import com.lib.retrofit.log.RetrofitCallback
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/login")
    fun loginUser(@Body body: JsonObject): Call<JSONObject>

    @POST("users/check")
    fun checkUser(@Body body: JsonObject): Call<JSONObject>

}
