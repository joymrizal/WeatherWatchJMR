package com.joymr.weatherwatchjmr

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Jika pengguna sudah login, arahkan ke WeatherWatchActivity
            val intent = Intent(this, WeatherWatchActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val registerLink: TextView = findViewById(R.id.register_link)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Please enter a valid email"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password must be at least 6 characters"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Pindah ke WeatherWatchActivity
                        val intent = Intent(this, WeatherWatchActivity::class.java)
                        startActivity(intent)
                        finish()  // Menghindari kembali ke MainActivity
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
