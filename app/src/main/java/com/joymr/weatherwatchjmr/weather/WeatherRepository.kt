package com.joymr.weatherwatchjmr.weather

import retrofit2.Response
import okhttp3.ResponseBody

class WeatherRepository {
    suspend fun getWeatherData(): Response<ResponseBody> {
        return RetrofitInstance.api.getWeatherData()
    }
}