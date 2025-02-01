package com.example.lisa

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.graphicsLayer


class MeditationActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    private lateinit var focusRequest: AudioFocusRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setOnAudioFocusChangeListener { focusChange ->
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    mediaPlayer?.pause()
                }
            }
            .build()

        mediaPlayer = MediaPlayer.create(this, R.raw.meditation_music).apply {
            isLooping = true
        }

        setContent {
            MeditationScreen(
                onPlay = {
                    if (audioManager?.requestAudioFocus(focusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        mediaPlayer?.start()
                    }
                },
                onPause = {
                    mediaPlayer?.pause()
                    audioManager?.abandonAudioFocusRequest(focusRequest)
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        audioManager?.abandonAudioFocusRequest(focusRequest)
    }
}

@Composable
fun MeditationScreen(onPlay: () -> Unit, onPause: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(300) } // 5 minutes in seconds

    val padding by animateDpAsState(targetValue = if (isPlaying) 32.dp else 16.dp)
    val backgroundColor by animateColorAsState(targetValue = if (isPlaying) Color(0xFFB2DFDB) else Color(0xFFE0F7FA))
    val scale by animateFloatAsState(targetValue = if (isPlaying) 1.1f else 1.0f)

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying && timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1
            }
            if (timeLeft == 0) {
                onPause()
                isPlaying = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isPlaying) "Relax & Breathe" else "Ready to Meditate?",
                fontSize = 24.sp,
                color = Color(0xFF00796B),
                modifier = Modifier
                    .padding(16.dp)
                    .graphicsLayer(
                        scaleX = if (isPlaying) 1.2f else 1f,
                        scaleY = if (isPlaying) 1.2f else 1f
                    )
            )


            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                fontSize = 32.sp,
                color = Color(0xFF00796B),
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = {
                    if (isPlaying) {
                        onPause()
                    } else {
                        onPlay()
                    }
                    isPlaying = !isPlaying
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                modifier = Modifier.graphicsLayer(
                    scaleX = if (isPlaying) 1.1f else 1f,
                    scaleY = if (isPlaying) 1.1f else 1f
                )
            ) {
                Text(text = if (isPlaying) "Pause" else "Start Meditation")
            }
        }
    }
}
