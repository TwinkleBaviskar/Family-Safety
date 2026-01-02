package com.example.familysafety

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DeleteAccountActivity : AppCompatActivity() {
    private lateinit var btnConfirmDelete: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        btnConfirmDelete = findViewById(R.id.btnConfirmDelete)
        btnCancel = findViewById(R.id.btnCancel)

        btnConfirmDelete.setOnClickListener {
            Toast.makeText(this, "Account Deleted Successfully", Toast.LENGTH_LONG).show()

            val intent = Intent(this, Splashscreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish() // just close this activity
        }
    }
}