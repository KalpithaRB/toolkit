package com.example.emergencytoolkit.components

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.emergencytoolkit.R

class AlertSoundActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_sound)

        val alertButton = findViewById<Button>(R.id.btnToggleAlert)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        alertButton.setOnClickListener {
            if (!isPlaying) {
                mediaPlayer = MediaPlayer.create(this, R.raw.siren)
                mediaPlayer?.start()
                isPlaying = true
                alertButton.text = "Stop Alert Sound"

                // Start vibration for 5 seconds in a pattern (e.g., 500ms on, 500ms off)
                val pattern = longArrayOf(0, 500, 500, 500, 500, 500) // Delay, Vibrate, Sleep, ...
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0)) // Repeat

            } else {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                isPlaying = false
                alertButton.text = "Play Alert Sound"

                // Stop vibration
                vibrator?.cancel()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()
    }
}
