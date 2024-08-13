package com.joymr.weatherwatchjmr.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import retrofit2.Response

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()
    val weatherCondition: MutableLiveData<String> = MutableLiveData()
    val temperature: MutableLiveData<String> = MutableLiveData()
    val humidity: MutableLiveData<String> = MutableLiveData()
    val windSpeed: MutableLiveData<String> = MutableLiveData()
    val windDirection: MutableLiveData<String> = MutableLiveData()

    fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                val response = repository.getWeatherData()
                if (response.isSuccessful) {
                    val html = response.body()?.string()
                    val document = Jsoup.parse(html)

                    // Mengambil elemen berdasarkan struktur HTML yang diberikan
                    val conditionElement: Element? = document.select("div.prakicu-kota div.service-block div p").first()
                    val temperatureElement: Element? = document.select("div.prakicu-kota div.service-block div h2.heading-md").first()
                    val humidityElement: Element? = document.select("div.prakicu-kota div.service-block div p").get(1)
                    val windSpeedElement: Element? = document.select("div.prakicu-kota div.service-block div p").get(2)
                    val windDirectionElement: Element? = document.select("div.prakicu-kota div.service-block div p").get(3)

                    // Menyimpan data ke LiveData
                    weatherCondition.postValue(conditionElement?.text() ?: "Data not available")
                    temperature.postValue(temperatureElement?.text() ?: "Data not available")
                    humidity.postValue(humidityElement?.text()?.replace("Kelembapan Udara: ", "") ?: "Data not available")
                    windSpeed.postValue(windSpeedElement?.text()?.replace("Kec. Angin: ", "") ?: "Data not available")
                    windDirection.postValue(windDirectionElement?.text()?.replace("Arah Angin dari: ", "") ?: "Data not available")
                } else {
                    weatherCondition.postValue("Error: ${response.code()}")
                    temperature.postValue("Error: ${response.code()}")
                    humidity.postValue("Error: ${response.code()}")
                    windSpeed.postValue("Error: ${response.code()}")
                    windDirection.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                weatherCondition.postValue("Exception: ${e.message}")
                temperature.postValue("Exception: ${e.message}")
                humidity.postValue("Exception: ${e.message}")
                windSpeed.postValue("Exception: ${e.message}")
                windDirection.postValue("Exception: ${e.message}")
            }
        }
    }
}
