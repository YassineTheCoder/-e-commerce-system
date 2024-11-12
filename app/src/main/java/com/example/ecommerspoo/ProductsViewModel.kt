package com.example.ecommerspoo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>> get() = _products

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://mocki.io/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiInterface::class.java)

    fun loadProducts() {
        apiService.getAllData().enqueue(object : Callback<List<Products>> {
            override fun onResponse(call: Call<List<Products>>, response: Response<List<Products>>) {
                if (response.isSuccessful) {
                    _products.value = response.body()
                } else {
                    // التعامل مع الأخطاء
                    _products.value = emptyList() // أو معالجة الخطأ كما تفضل
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                // التعامل مع الفشل في الطلب
                _products.value = emptyList() // أو معالجة الفشل كما تفضل
            }
        })
    }
}
