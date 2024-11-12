package com.example.ecommerspoo

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {
    @GET("/v1/2710bcef-fcab-44c5-937b-076dad84580d")
    fun getAllData(): Call<ApiInterface>
}