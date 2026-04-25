package com.leeonisrael.cockyclicker.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.leeonisrael.cockyclicker.R

/**
 * Manages all game sound effects via SoundPool (low latency).
 */
class SoundManager(context: Context) {

    var isMuted: Boolean = false
        set(value) {
            field = value
            if (value) {
                backgroundPlayer?.pause()
            } else {
                backgroundPlayer?.start()
            }
        }

    private var backgroundPlayer: MediaPlayer? = null
    
    init {
        backgroundPlayer = MediaPlayer.create(context, R.raw.background_theme).apply {
            isLooping = true
            setVolume(0.5f, 0.5f)
        }
        if (!isMuted) {
            backgroundPlayer?.start()
        }
    }

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(6)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    // Uncomment once audio files are placed in res/raw/:
    private val tapSoundId      = soundPool.load(context, R.raw.tap_sound, 1)
    private val upgradeSoundId  = soundPool.load(context, R.raw.upgrade_sound, 1)
    private val prestigeSoundId = soundPool.load(context, R.raw.prestige_sound, 1)
    private val milestoneSoundId = soundPool.load(context, R.raw.milestone_sound, 1)

    fun playTap() {
        if (isMuted) return
        soundPool.play(tapSoundId, 1f, 1f, 0, 0, 1.0f)
    }

    fun playUpgrade() {
        if (isMuted) return
        soundPool.play(upgradeSoundId, 1f, 1f, 0, 0, 1.0f)
    }

    fun playPrestige() {
        if (isMuted) return
        soundPool.play(prestigeSoundId, 1f, 1f, 1, 0, 1.0f)
    }

    fun playMilestone() {
        if (isMuted) return
        soundPool.play(milestoneSoundId, 1f, 1f, 0, 0, 1.0f)
    }

    fun playBackground() {
        if (!isMuted && backgroundPlayer?.isPlaying == false) {
            backgroundPlayer?.start()
        }
    }

    fun pauseBackground() {
        backgroundPlayer?.pause()
    }

    fun release() {
        backgroundPlayer?.stop()
        backgroundPlayer?.release()
        backgroundPlayer = null
        soundPool.release()
    }
}
