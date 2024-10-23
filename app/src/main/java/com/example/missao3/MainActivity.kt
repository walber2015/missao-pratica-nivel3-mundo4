package com.example.missao3

import AudioHelper
import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var audioHelper: AudioHelper
    private lateinit var audioManager: AudioManager
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        audioHelper = AudioHelper(this)


        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager


        mediaPlayer = MediaPlayer.create(this, R.raw.audio)


        val playButton: Button = findViewById(R.id.button_play_audio)
        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }


        val isSpeakerAvailable = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)
        if (isSpeakerAvailable) {
            println("Alto-falante embutido disponível.")
        } else {
            println("Alto-falante embutido não disponível.")
        }


        val isBluetoothHeadsetConnected = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
        if (isBluetoothHeadsetConnected) {
            println("Fone de ouvido Bluetooth conectado.")
        } else {
            println("Fone de ouvido Bluetooth não conectado.")
        }


        registerAudioDeviceCallback()
    }

    private fun registerAudioDeviceCallback() {
        audioManager.registerAudioDeviceCallback(object : AudioDeviceCallback() {

            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>) {
                super.onAudioDevicesAdded(addedDevices)
                if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    println("Fone de ouvido Bluetooth foi conectado.")
                }
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>) {
                super.onAudioDevicesRemoved(removedDevices)
                if (!audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    println("Fone de ouvido Bluetooth foi desconectado.")
                }
            }
        }, null)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}