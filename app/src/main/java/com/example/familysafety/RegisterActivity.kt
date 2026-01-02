package com.example.familysafety

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.familysafety.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnclick.setOnClickListener {
            if (validateInput()) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        val username = binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.username).text.toString().trim()
        val mobile = binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.mobile).text.toString().trim()
        val password = binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.password).text.toString().trim()
        val confirmPassword = binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.confirmPassword).text.toString().trim()

        if (username.isEmpty()) {
            binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.username).error = "Username is required"
            return false
        }

        if (mobile.isEmpty() || !mobile.matches(Regex("^[0-9]{10}$"))) {
            binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.mobile).error = "Enter valid 10-digit mobile number"
            return false
        }

        if (password.length < 6) {
            binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.password).error = "Password must be at least 6 characters"
            return false
        }

        if (password != confirmPassword) {
            binding.root.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.confirmPassword).error = "Passwords do not match"
            return false
        }

        return true
    }
}
