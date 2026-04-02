package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem

interface PlatformPlayer {
    fun prepare(item: MediaItem, autoplay: Boolean)
    fun play()
    fun pause()
    fun stop()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    fun setPlaybackSpeed(speed: Float)
    fun release()
}
