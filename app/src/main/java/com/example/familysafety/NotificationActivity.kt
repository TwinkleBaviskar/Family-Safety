package com.example.familysafety

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NotificationActivity : AppCompatActivity() {

    private lateinit var switchAll: SwitchCompat
    private lateinit var switchGroup: SwitchCompat
    private lateinit var switchMessages: SwitchCompat
    private lateinit var switchCritical: SwitchCompat
    private lateinit var switchFailure: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        switchAll = findViewById(R.id.switchAllNotifications)
        switchGroup = findViewById(R.id.switchGroup)
        switchMessages = findViewById(R.id.switchMessages)
        switchCritical = findViewById(R.id.switchCritical)
        switchFailure = findViewById(R.id.switchFailure)

        val prefs = getSharedPreferences("NotificationPrefs", MODE_PRIVATE)

        // Restore states
        switchAll.isChecked = prefs.getBoolean("all_notifications", true)
        switchGroup.isChecked = prefs.getBoolean("group_notifications", true)
        switchMessages.isChecked = prefs.getBoolean("message_notifications", true)
        switchCritical.isChecked = prefs.getBoolean("critical_alerts", true)
        switchFailure.isChecked = prefs.getBoolean("failure_alerts", true)

        // Enable/disable sub switches if "All" is off
        setAllSwitchesEnabled(switchAll.isChecked)

        switchAll.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("all_notifications", isChecked).apply()
            setAllSwitchesEnabled(isChecked)
        }

        switchGroup.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("group_notifications", isChecked).apply()
        }

        switchMessages.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("message_notifications", isChecked).apply()
        }

        switchCritical.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("critical_alerts", isChecked).apply()
        }

        switchFailure.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("failure_alerts", isChecked).apply()
        }
    }

    private fun setAllSwitchesEnabled(enabled: Boolean) {
        switchGroup.isEnabled = enabled
        switchMessages.isEnabled = enabled
        switchCritical.isEnabled = enabled
        switchFailure.isEnabled = enabled
    }
}