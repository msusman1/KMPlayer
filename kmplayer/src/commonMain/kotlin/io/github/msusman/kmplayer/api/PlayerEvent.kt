package io.github.msusman.kmplayer.api

sealed class PlayerEvent {
    data class StateChanged(val state: PlayerState) : PlayerEvent()
    data class PositionChanged(val positionMs: Long) : PlayerEvent()
    data class TrackChanged(val item: MediaItem) : PlayerEvent()
    data class PlaylistChanged(val playlist: Playlist) : PlayerEvent()
    data class PlaybackModeChanged(val mode: PlaybackMode) : PlayerEvent()
    data class VolumeChanged(val volume: Float) : PlayerEvent()
    data class PlaybackSpeedChanged(val speed: Float) : PlayerEvent()
    data class ErrorOccurred(val error: PlayerError) : PlayerEvent()
}
