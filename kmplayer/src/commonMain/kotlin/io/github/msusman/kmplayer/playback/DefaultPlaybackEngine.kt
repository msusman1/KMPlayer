package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.cache.CacheManager
import io.github.msusman.kmplayer.logging.Logger

internal class DefaultPlaybackEngine(
    private val cacheManager: CacheManager?,
    private val logger: Logger?
) : PlaybackEngine {
    override fun prepare(item: MediaItem, autoplay: Boolean) {
        logger?.d("DefaultPlaybackEngine", "prepare: ${item.id}")
        // Placeholder. PlatformPlayer integration is implemented in platform source sets.
    }

    override fun play() {
        logger?.d("DefaultPlaybackEngine", "play")
    }

    override fun pause() {
        logger?.d("DefaultPlaybackEngine", "pause")
    }

    override fun stop() {
        logger?.d("DefaultPlaybackEngine", "stop")
    }

    override fun seekTo(positionMs: Long) {
        logger?.d("DefaultPlaybackEngine", "seekTo: $positionMs")
    }

    override fun setVolume(volume: Float) {
        logger?.d("DefaultPlaybackEngine", "setVolume: $volume")
    }

    override fun setPlaybackSpeed(speed: Float) {
        logger?.d("DefaultPlaybackEngine", "setPlaybackSpeed: $speed")
    }

    override fun release() {
        logger?.d("DefaultPlaybackEngine", "release")
    }
}
