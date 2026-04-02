package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

actual fun createPlatformPlayer(
    cachePolicy: CachePolicy,
    logger: Logger?
): PlatformPlayer = IOSPlatformPlayer(cachePolicy, logger)

internal class IOSPlatformPlayer(
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlatformPlayer {
    override fun prepare(item: io.github.msusman.kmplayer.api.MediaItem, autoplay: Boolean) {
        logger?.d("IOSPlatformPlayer", "prepare: ${item.id}")
    }

    override fun play() {
        logger?.d("IOSPlatformPlayer", "play")
    }

    override fun pause() {
        logger?.d("IOSPlatformPlayer", "pause")
    }

    override fun stop() {
        logger?.d("IOSPlatformPlayer", "stop")
    }

    override fun seekTo(positionMs: Long) {
        logger?.d("IOSPlatformPlayer", "seekTo: $positionMs")
    }

    override fun setVolume(volume: Float) {
        logger?.d("IOSPlatformPlayer", "setVolume: $volume")
    }

    override fun setPlaybackSpeed(speed: Float) {
        logger?.d("IOSPlatformPlayer", "setPlaybackSpeed: $speed")
    }

    override fun release() {
        logger?.d("IOSPlatformPlayer", "release")
    }
}
