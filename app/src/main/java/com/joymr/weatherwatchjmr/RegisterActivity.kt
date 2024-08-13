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

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.register_email)
        val passwordEditText: EditText = findViewById(R.id.register_password)
        val registerButton: Button = findViewById(R.id.register_button)
        val loginLink: TextView = findViewById(R.id.login_link)

        registerButton.setOnClickListener {
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

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        finish() // Close RegisterActivity
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}