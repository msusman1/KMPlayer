package com.kmplayer.player

interface PlaybackEngine {
    fun prepare(track: Track): Boolean
    fun play()
    fun pause()
    fun stop()
    fun release()
    val isPlaying: Boolean
}
