package io.github.msusman.kmplayer.api

sealed class PlayerState {
    object Idle : PlayerState()

    data class Loading(
        val item: MediaItem
    ) : PlayerState()

    data class Ready(
        val item: MediaItem,
        val durationMs: Long,
        val positionMs: Long
    ) : PlayerState()

    data class Playing(
        val item: MediaItem,
        val durationMs: Long,
        val positionMs: Long,
        val speed: Float
    ) : PlayerState()

    data class Paused(
        val item: MediaItem,
        val durationMs: Long,
        val positionMs: Long
    ) : PlayerState()

    data class Buffering(
        val item: MediaItem,
        val durationMs: Long,
        val positionMs: Long,
        val bufferPercent: Int
    ) : PlayerState()

    data class Stopped(
        val item: MediaItem?,
        val durationMs: Long,
        val positionMs: Long
    ) : PlayerState()

    data class Completed(
        val item: MediaItem,
        val durationMs: Long
    ) : PlayerState()

    data class Error(
        val error: PlayerError,
        val lastKnownState: PlayerState? = null
    ) : PlayerState()
}
