package com.murali.test.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("api/GetDetails?")
    suspend fun getAllStudents(@Query("code", encoded = true) code: String): JsonObject

    companion object {
        var retrofitService: APIService? = null
        fun getInstance(): APIService {
            if (retrofitService == null) {

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://bvassignment.azurewebsites.net/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(APIService::class.java)
            }
            return retrofitService!!
        }
    }
}
