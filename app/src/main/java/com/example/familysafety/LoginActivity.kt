package com.example.familysafety

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.familysafety.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.btnclick.setOnClickListener {
            val email = loginBinding.emailField.text.toString().trim()
            val password = loginBinding.passwordField.text.toString().trim()

            if (email.isEmpty()) {
                loginBinding.emailField.error = "Email is required"
                loginBinding.emailField.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginBinding.emailField.error = "Enter a valid email"
                loginBinding.emailField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                loginBinding.passwordField.error = "Password is required"
                loginBinding.passwordField.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                loginBinding.passwordField.error = "Password must be at least 6 characters"
                loginBinding.passwordField.requestFocus()
                return@setOnClickListener
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loginBinding.txtclick.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
