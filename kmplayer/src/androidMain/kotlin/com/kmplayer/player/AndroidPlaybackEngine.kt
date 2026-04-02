package com.kmplayer.player

import android.media.MediaPlayer

internal class AndroidPlaybackEngine : PlaybackEngine {
    private val mediaPlayer = MediaPlayer()

    override val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    override fun prepare(track: Track): Boolean {
        return try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.url)
            mediaPlayer.prepare()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    override fun release() {
        mediaPlayer.release()
    }
}

actual fun createPlaybackEngine(): PlaybackEngine = AndroidPlaybackEngine()
