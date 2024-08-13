package com.joymr.weatherwatchjmr.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.bmkg.go.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: BMKGApi by lazy {
        retrofit.create(BMKGApi::class.java)
    }
}