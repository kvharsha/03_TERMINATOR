// HomeActivity.kt
package com.example.lisa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val stressButton = findViewById<Button>(R.id.stressButton)
        val meditationButton = findViewById<Button>(R.id.meditationButton)
        val focusButton = findViewById<Button>(R.id.focusButton)

        stressButton.setOnClickListener {
            val intent = Intent(this, StressActivity::class.java)
            startActivity(intent)
        }

        meditationButton.setOnClickListener {
            val intent = Intent(this, MeditationActivity::class.java)
            startActivity(intent)
        }

        focusButton.setOnClickListener {
            val intent = Intent(this, FocusActivity::class.java)
            startActivity(intent)
        }
    }
}
