package com.leeonisrael.cockyclicker.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * Manages all game sound effects via SoundPool (low latency).
 *
 * To activate sounds, add the following audio files to res/raw/:
 *   res/raw/tap_sound.ogg      — short chicken cluck or pop (< 0.3 s)
 *   res/raw/upgrade_sound.ogg  — satisfying cha-ching or level-up chime (< 0.5 s)
 *   res/raw/prestige_sound.ogg — triumphant fanfare or rooster crow (< 2 s)
 *   res/raw/milestone_sound.ogg — quick ascending chime (< 0.5 s)
 *
 * Then uncomment the load() calls and play() calls below.
 */
class SoundManager(context: Context) {

    var isMuted: Boolean = false

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
    // private val tapSoundId      = soundPool.load(context, R.raw.tap_sound, 1)
    // private val upgradeSoundId  = soundPool.load(context, R.raw.upgrade_sound, 1)
    // private val prestigeSoundId = soundPool.load(context, R.raw.prestige_sound, 1)
    // private val milestoneSoundId = soundPool.load(context, R.raw.milestone_sound, 1)

    fun playTap() {
        if (isMuted) return
        // soundPool.play(tapSoundId, 0.7f, 0.7f, 0, 0, 1.0f)
    }

    fun playUpgrade() {
        if (isMuted) return
        // soundPool.play(upgradeSoundId, 1f, 1f, 0, 0, 1.0f)
    }

    fun playPrestige() {
        if (isMuted) return
        // soundPool.play(prestigeSoundId, 1f, 1f, 1, 0, 1.0f)
    }

    fun playMilestone() {
        if (isMuted) return
        // soundPool.play(milestoneSoundId, 1f, 1f, 0, 0, 1.0f)
    }

    fun release() {
        soundPool.release()
    }
}
