package com.joymr.weatherwatchjmr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.joymr.weatherwatchjmr.weather.WeatherViewModel

class WeatherWatchActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: WeatherViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_watch)

        auth = FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        val welcomeTextView: TextView = findViewById(R.id.welcome_text)
        val logoutButton: Button = findViewById(R.id.logout_button)

        welcomeTextView.text = "Welcome, ${currentUser?.email}!"

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Weather Watch
        val tvWeatherCondition = findViewById<TextView>(R.id.tvWeatherCondition)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvWindSpeed = findViewById<TextView>(R.id.tvWindSpeed)
        val tvWindDirection = findViewById<TextView>(R.id.tvWindDirection)
        val tvAdvice = findViewById<TextView>(R.id.tvAdvice)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        viewModel.weatherCondition.observe(this) { data ->
            tvWeatherCondition.text = "Kondisi Cuaca: $data"
        }

        viewModel.temperature.observe(this) { data ->
            val temperatureString = data.replace("°C", "").trim()
            tvTemperature.text = "Suhu: $data"
            val temperature = temperatureString.toIntOrNull() ?: 0
            val tempAdvice = when {
                temperature < 15 -> "Cuaca dingin, Disarankan untuk memakai pakaian hangat."
                temperature in 15..25 -> "Cuaca cukup sejuk, Pakaian ringan atau sweater bisa nyaman digunakan."
                else -> "Cuaca panas, Pastikan untuk menggunakan pakaian ringan dan tetap terhidrasi."
            }
            tvAdvice.text = tempAdvice
        }

        viewModel.humidity.observe(this) { data ->
            val humidityString = data.replace("%", "").trim()
            tvHumidity.text = "Kelembapan Udara: $data"
            val humidity = humidityString.toIntOrNull() ?: 0
            val humidityAdvice = when {
                humidity < 30 -> "Kelembapan rendah, Pastikan untuk tetap terhidrasi dan gunakan pelembap udara."
                humidity in 30..60 -> "Kelembapan normal, Tidak perlu perhatian khusus."
                else -> "Kelembapan tinggi, Gunakan dehumidifier jika diperlukan dan hindari lingkungan yang lembap."
            }
            tvAdvice.text = "${tvAdvice.text}\n$humidityAdvice"
        }

        viewModel.windSpeed.observe(this) { data ->
            val windSpeedString = data.replace(" km/jam", "").trim()
            tvWindSpeed.text = "Kecepatan Angin: $data"
            val windSpeed = windSpeedString.toIntOrNull() ?: 0
            val windSpeedAdvice = when {
                windSpeed < 15 -> "Angin ringan, Tidak perlu khawatir tentang angin."
                windSpeed in 15..30 -> "Angin sedang, Jika beraktivitas di luar, pastikan untuk berpakaian sesuai."
                else -> "Angin kencang, Hati-hati saat beraktivitas di luar dan pastikan untuk mengamankan barang-barang."
            }
            tvAdvice.text = "${tvAdvice.text}\n$windSpeedAdvice"
        }

        viewModel.windDirection.observe(this) { data ->
            val windDirectionString = data.toString()
            tvWindDirection.text = "Arah Angin: $windDirectionString"
            val windDirectionAdvice = when {
                "utara" in windDirectionString.lowercase() -> "Angin dari utara, Tidak ada saran khusus."
                "selatan" in windDirectionString.lowercase() -> "Angin dari selatan, Sesuaikan pakaian jika perlu."
                "timur" in windDirectionString.lowercase() -> "Angin dari timur, Perhatikan cuaca sekitar."
                "barat" in windDirectionString.lowercase() -> "Angin dari barat, Tidak ada saran khusus."
                else -> "Arah angin tidak dikenal, Sesuaikan diri dengan kondisi cuaca secara umum."
            }
            tvAdvice.text = "${tvAdvice.text}\n$windDirectionAdvice"
        }

        viewModel.fetchWeatherData()
    }
}


