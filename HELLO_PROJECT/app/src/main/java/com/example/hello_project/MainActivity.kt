package com.example.hello_project

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.width
import android.os.Handler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import kotlin.math.round
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp


class MainActivity : AppCompatActivity() {
    private lateinit var soundPool: SoundPool
//    private var soundId: Int = 0
//    val current_streamId_list = mutableListOf<Int>()
    val current_handler_list = mutableListOf<Handler>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ----------------------- SOUNDPOOL SETUP -----------------------
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        val loaded_snrhigh = soundPool.load(this, R.raw.hi1, 1)
        val loaded_snrlow = soundPool.load(this, R.raw.snr_high, 1)
        soundPool.play(loaded_snrhigh, 0f, 0f, 1, 0, 1f)
        soundPool.play(loaded_snrlow, 0f, 0f, 1, 0, 1f)

        setContent {
            MainUI(soundPool, loaded_snrhigh, loaded_snrlow)
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
                .padding(18.dp)
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
                .padding(18.dp)
        ) {
            Button(onClick = onClick) {
                Text(text = "Stop Sound")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NumeratorDropdown(selectedOptionNumerator: MutableState<String>) {
        val options = listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17")
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOptionNumerator.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .width(90.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOptionNumerator.value = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DenominatorDropdown(selectedOptionDenominator: MutableState<String>) {
        val options = listOf("2", "4", "8", "16", "32")
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOptionDenominator.value,
                onValueChange = {},
                readOnly = true,
//                label = { Text("Pick a fruit") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .width(90.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOptionDenominator.value = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun ToggleSwitch(isToggled: MutableState<Boolean>) {
        Switch(
            checked = isToggled.value,
            onCheckedChange = { isToggled.value = it }
        )
    }



//  -----------------------------------------------------------------
//  ---------------------------- MAIN UI ----------------------------
//  -----------------------------------------------------------------
    @Composable
    fun MainUI(soundPool: SoundPool, loaded_snrhigh: Int, loaded_snrlow: Int) {
        var textBPM by remember { mutableStateOf("120") }
        var selectedOptionNumerator = remember { mutableStateOf("4") }
//        var selectedOptionDenominator = remember { mutableStateOf("4") }
        var isToggled = remember { mutableStateOf(true) }

        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    value = textBPM,
                    onValueChange = { textBPM = it },
//                    state = rememberTextFieldState(),
                    label = { Text("BPM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(150.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text="Include beat   ", color=Color.White, fontSize = 20.sp)
                    ToggleSwitch(isToggled)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text="Beat:  ", color=Color.White, fontSize = 20.sp)
                    NumeratorDropdown(selectedOptionNumerator)
//                    Text(text = " / ", color = Color.White, fontSize = 33.sp)
//                    DenominatorDropdown(selectedOptionDenominator)
                }

                Row {
                    StartSoundButton(onClick = {
                        println("BAHH")
                        val current_BPM = textBPM.toInt()
                        val customLoopIntervalMs = round(1000.0/(current_BPM/60.0)).toLong()
                        val handler = Handler(Looper.getMainLooper())
//                        val handlerwholeapp = Handler(Looper.getMainLooper())
                        current_handler_list.addLast(handler)

                        val numerator = selectedOptionNumerator.value.toInt()
                        val includeBeat = isToggled.value
//                        val denominator = selectedOptionDenominator.value.toInt()


//                        println(customLoopIntervalMs)
                        println(numerator)
//                        println(includeBeat)

                        if(includeBeat) {
//                            println("Hello beat")
                            var iter = 1
                            fun playBeat() {
//                                println(iter)
                                if(iter!=1) {
                                    soundPool.play(loaded_snrhigh, 1f, 1f, 1, 0, 1f)
                                    if(iter==numerator){
                                        iter = 1
                                    } else {
                                        iter += 1
                                    }
                                } else {
                                    soundPool.play(loaded_snrlow, 1f, 1f, 1, 0, 1f)
                                    iter += 1
                                }
                                handler.postDelayed({
                                    playBeat()
                                }, customLoopIntervalMs)
                            }
                            playBeat()
                        //wysrane, to już jest zrobione (schowaj kod)
                        } else {
//                            println("Hello metronome")
                            fun playMetronome() {
                                soundPool.play(loaded_snrhigh, 1f, 1f, 1, 0, 1f)
                                handler.postDelayed({
                                    playMetronome()
                                }, customLoopIntervalMs)
                            }
                            playMetronome()
                        }

                        println("BWAHH")
                    })

                    StopSoundButton(onClick = {
                        while(current_handler_list.size > 0){
                            current_handler_list[0].removeCallbacksAndMessages(null) // cancel future replays
                            current_handler_list.removeFirst()
                        }
                        println("BAHH2")
                    })
                }
            }
        }
    }
}