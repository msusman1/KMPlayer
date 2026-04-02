package io.github.msusman.kmplayer.core

import io.github.msusman.kmplayer.api.KMPlayerConfig
import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlaybackMode
import io.github.msusman.kmplayer.api.PlayerEvent
import io.github.msusman.kmplayer.api.PlayerListener
import io.github.msusman.kmplayer.api.PlayerState
import io.github.msusman.kmplayer.api.Playlist
import io.github.msusman.kmplayer.playback.DefaultPlaybackEngine
import io.github.msusman.kmplayer.playback.PlaybackEngine
import io.github.msusman.kmplayer.playlist.DefaultPlaylistManager
import io.github.msusman.kmplayer.playlist.PlaylistManager

internal class MusicPlayerImpl(
    private val config: KMPlayerConfig
) : MusicPlayer {
    private val listeners = LinkedHashSet<PlayerListener>()

    private val engine: PlaybackEngine = DefaultPlaybackEngine(
        context = config.platformContext,
        cachePolicy = config.cachePolicy,
        logger = config.logger
    )

    private val playlists: PlaylistManager = DefaultPlaylistManager()

    private var state: PlayerState = PlayerState.Idle

    init {
        playlists.setPlaybackMode(config.defaultPlaybackMode)
        engine.setListener(object : PlaybackEngine.Listener {
            override fun onBuffering(item: MediaItem, durationMs: Long, positionMs: Long, bufferPercent: Int) {
                state = PlayerState.Buffering(item, durationMs, positionMs, bufferPercent)
                emitState()
            }

            override fun onReady(item: MediaItem, durationMs: Long, positionMs: Long) {
                state = PlayerState.Ready(item, durationMs, positionMs)
                emitState()
            }

            override fun onPlaying(item: MediaItem, durationMs: Long, positionMs: Long, speed: Float) {
                state = PlayerState.Playing(item, durationMs, positionMs, speed)
                emitState()
            }

            override fun onPaused(item: MediaItem, durationMs: Long, positionMs: Long) {
                state = PlayerState.Paused(item, durationMs, positionMs)
                emitState()
            }

            override fun onCompleted(item: MediaItem, durationMs: Long) {
                state = PlayerState.Completed(item, durationMs)
                emitState()
            }

            override fun onError(item: MediaItem?, error: io.github.msusman.kmplayer.api.PlayerError) {
                state = PlayerState.Error(error, lastKnownState = state)
                emitState()
                emitEvent(PlayerEvent.ErrorOccurred(error))
            }

            override fun onPosition(positionMs: Long) {
                emitEvent(PlayerEvent.PositionChanged(positionMs))
            }
        })
    }

    override fun load(item: MediaItem, autoplay: Boolean) {
        state = PlayerState.Loading(item)
        emitState()
        engine.prepare(item, autoplay)
    }

    override fun setPlaylist(playlist: Playlist, startIndex: Int, autoplay: Boolean) {
        playlists.setPlaylist(playlist, startIndex)
        emitEvent(PlayerEvent.PlaylistChanged(playlist))
        playlists.currentItem()?.let { load(it, autoplay) }
    }

    override fun play() {
        engine.play()
    }

    override fun pause() {
        engine.pause()
    }

    override fun stop() {
        engine.stop()
    }

    override fun seekTo(positionMs: Long) {
        engine.seekTo(positionMs)
    }

    override fun skipNext() {
        playlists.nextItem()?.let { load(it, autoplay = true) }
    }

    override fun skipPrevious() {
        playlists.previousItem()?.let { load(it, autoplay = true) }
    }

    override fun setPlaybackMode(mode: PlaybackMode) {
        playlists.setPlaybackMode(mode)
        emitEvent(PlayerEvent.PlaybackModeChanged(mode))
    }

    override fun setVolume(volume: Float) {
        engine.setVolume(volume)
        emitEvent(PlayerEvent.VolumeChanged(volume))
    }

    override fun setPlaybackSpeed(speed: Float) {
        engine.setPlaybackSpeed(speed)
        emitEvent(PlayerEvent.PlaybackSpeedChanged(speed))
    }

    override fun currentState(): PlayerState = state

    override fun addListener(listener: PlayerListener) {
        listeners.add(listener)
        listener.onStateChanged(state)
    }

    override fun removeListener(listener: PlayerListener) {
        listeners.remove(listener)
    }

    override fun release() {
        engine.release()
        listeners.clear()
    }

    private fun emitState() {
        listeners.forEach { it.onStateChanged(state) }
        listeners.forEach { it.onEvent(PlayerEvent.StateChanged(state)) }
    }

    private fun emitEvent(event: PlayerEvent) {
        listeners.forEach { it.onEvent(event) }
    }
}
