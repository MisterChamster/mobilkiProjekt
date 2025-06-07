package com.example.hello_project

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp



class MainActivity : AppCompatActivity() {
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    val current_soundId_list = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ----------------------- SOUNDPOOL SETUP -----------------------
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
        val loaded_snrlow = soundPool.load(this, R.raw.snr_low, 1)
        val loaded_snrhigh = soundPool.load(this, R.raw.snr_high, 1)

        setContent {
//            var textBPM = remember { mutableIntStateOf(120) }
            MainUI(soundPool, loaded_snrlow)
        }
    }

    override fun onDestroy() {
        soundPool.release()
        super.onDestroy()
    }

    @Composable
    fun StartSoundButton(onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onClick) {
                Text(text = "Play Sound")
            }
        }
    }

    @Composable
    fun StopSoundButton(onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onClick) {
                Text(text = "Stop Sound")
            }
        }
    }

    @Composable
    fun MainUI(soundPool: SoundPool, soundId: Int) {
        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    state = rememberTextFieldState(),
                    label = { Text("BPM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.White,
                        focusedTextColor = Color.White
                    )
                )


                Row {
                    StartSoundButton(onClick = {
                        var a = soundPool.play(soundId, 1f, 1f, 0, -1, 1f)
                        current_soundId_list.addLast(a)
                        println("BAHH")
                        println(a)
                        println("BWAHH")
                    })
//                Spacer(modifier = Modifier.height(10.dp))

                    StopSoundButton(onClick = {
                        while(current_soundId_list.size > 0){
                            soundPool.stop(current_soundId_list[0])
                            current_soundId_list.removeFirst()
                        }
                        println("BAHH2")
                    })
                }
            }
        }
    }
}




