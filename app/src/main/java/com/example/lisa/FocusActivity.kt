package com.example.lisa

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FocusActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus)  // Ensure this layout has correct IDs

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val timeInput = findViewById<EditText>(R.id.timeInput)
        val startFocusButton = findViewById<Button>(R.id.startFocusButton)

        startFocusButton.setOnClickListener {
            val timeText = timeInput.text.toString()
            if (timeText.isEmpty()) {
                Toast.makeText(this, "Please enter focus time in minutes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (notificationManager.isNotificationPolicyAccessGranted) {
                enableDND(timeText.toInt())
            } else {
                Toast.makeText(this, "Grant DND access permission", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun enableDND(duration: Int) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        Toast.makeText(this, "Focus mode enabled for $duration minutes", Toast.LENGTH_SHORT).show()

        // Disable DND after specified time
        Handler().postDelayed({
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            Toast.makeText(this, "Focus session ended", Toast.LENGTH_SHORT).show()
        }, duration * 60 * 1000L)
    }
}
