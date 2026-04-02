package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem

interface PlaybackEngine {
    fun prepare(item: MediaItem, autoplay: Boolean)
    fun play()
    fun pause()
    fun stop()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    fun setPlaybackSpeed(speed: Float)
    fun release()
    fun setListener(listener: Listener?)

    interface Listener {
        fun onBuffering(item: MediaItem, durationMs: Long, positionMs: Long, bufferPercent: Int)
        fun onReady(item: MediaItem, durationMs: Long, positionMs: Long)
        fun onPlaying(item: MediaItem, durationMs: Long, positionMs: Long, speed: Float)
        fun onPaused(item: MediaItem, durationMs: Long, positionMs: Long)
        fun onCompleted(item: MediaItem, durationMs: Long)
        fun onError(item: MediaItem?, error: io.github.msusman.kmplayer.api.PlayerError)
        fun onPosition(positionMs: Long)
    }
}
