package com.example.hello_project

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup
import android.content.Context
import com.example.hello_project.R

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create layout in code
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Create button in code
        val button = Button(this).apply {
            text = "Play Sound"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        layout.addView(button)
        setContentView(layout)

        // SoundPool setup
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sound from res/raw/sound.mp3
        soundId = soundPool.load(this, R.raw.snare, 1)

        button.setOnClickListener {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
