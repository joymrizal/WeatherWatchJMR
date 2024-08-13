package com.joymr.weatherwatchjmr.weather

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface BMKGApi {
    @GET("cuaca/prakiraan-cuaca.bmkg?Kota=Bandung&AreaID=501212&Prov=35")
    suspend fun getWeatherData(): Response<ResponseBody>
}