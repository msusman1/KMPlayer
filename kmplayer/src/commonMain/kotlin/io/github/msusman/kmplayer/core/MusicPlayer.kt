package io.github.msusman.kmplayer.core

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlaybackMode
import io.github.msusman.kmplayer.api.PlayerListener
import io.github.msusman.kmplayer.api.PlayerState
import io.github.msusman.kmplayer.api.Playlist

internal interface MusicPlayer {
    fun load(item: MediaItem, autoplay: Boolean)
    fun setPlaylist(playlist: Playlist, startIndex: Int, autoplay: Boolean)

    fun play()
    fun pause()
    fun stop()
    fun seekTo(positionMs: Long)
    fun skipNext()
    fun skipPrevious()

    fun setPlaybackMode(mode: PlaybackMode)
    fun setVolume(volume: Float)
    fun setPlaybackSpeed(speed: Float)

    fun currentState(): PlayerState
    fun addListener(listener: PlayerListener)
    fun removeListener(listener: PlayerListener)

    fun release()
}
