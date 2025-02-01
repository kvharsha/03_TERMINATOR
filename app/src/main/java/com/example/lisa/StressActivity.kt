package com.example.lisa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class StressActivity : AppCompatActivity() {
    private lateinit var mediaRecorder: MediaRecorder
    private val REQUEST_MIC_PERMISSION = 200
    private var isRecording = false
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stress)

        val submitButton = findViewById<Button>(R.id.submitButton)
        handler = Handler(Looper.getMainLooper())

        submitButton.setOnClickListener {
            if (checkMicPermission()) {
                measureLoudness()
            } else {
                requestMicPermission()
            }
        }
    }

    private fun checkMicPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestMicPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_MIC_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_MIC_PERMISSION && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            measureLoudness()
        } else {
            Toast.makeText(this, "Microphone permission is required!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun measureLoudness() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile("/dev/null")
            prepare()
            start()
        }

        isRecording = true
        handler.postDelayed(checkLoudness, 1000) // Check every second
    }

    private val checkLoudness = object : Runnable {
        override fun run() {
            if (!isRecording) return

            val maxAmplitude = mediaRecorder.maxAmplitude
            if (maxAmplitude > 0) {
                val db = 20 * Math.log10(maxAmplitude.toDouble())
                if (db > 50) {
                    navigateToActivity(FunnyARActivity::class.java, "models/funny_character.glb")
                } else {
                    navigateToActivity(ScenicARActivity::class.java, "models/scenic_view.glb")
                }
            } else {
                Toast.makeText(this@StressActivity, "No sound detected, try again!", Toast.LENGTH_SHORT).show()
                stopRecording()
            }
        }
    }

    private fun navigateToActivity(activityClass: Class<*>, modelPath: String) {
        if (isRecording) {
            stopRecording()
            val intent = Intent(this, activityClass).apply {
                putExtra("MODEL_URI", modelPath)
            }
            startActivity(intent)
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop()
                mediaRecorder.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRecording = false
        }
    }
}
