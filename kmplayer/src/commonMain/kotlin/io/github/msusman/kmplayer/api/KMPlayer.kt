package io.github.msusman.kmplayer.api

import io.github.msusman.kmplayer.core.MusicPlayer

class KMPlayer internal constructor(
    private val impl: MusicPlayer
) {
    fun load(item: MediaItem, autoplay: Boolean = false) = impl.load(item, autoplay)
    fun setPlaylist(playlist: Playlist, startIndex: Int = 0, autoplay: Boolean = false) =
        impl.setPlaylist(playlist, startIndex, autoplay)

    fun play() = impl.play()
    fun pause() = impl.pause()
    fun stop() = impl.stop()
    fun seekTo(positionMs: Long) = impl.seekTo(positionMs)
    fun skipNext() = impl.skipNext()
    fun skipPrevious() = impl.skipPrevious()

    fun setPlaybackMode(mode: PlaybackMode) = impl.setPlaybackMode(mode)
    fun setVolume(volume: Float) = impl.setVolume(volume)
    fun setPlaybackSpeed(speed: Float) = impl.setPlaybackSpeed(speed)

    fun currentState(): PlayerState = impl.currentState()
    fun addListener(listener: PlayerListener) = impl.addListener(listener)
    fun removeListener(listener: PlayerListener) = impl.removeListener(listener)

    fun release() = impl.release()
}

interface PlayerListener {
    fun onStateChanged(state: PlayerState) {}
    fun onEvent(event: PlayerEvent) {}
}
