package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

actual fun createPlatformPlayer(
    cachePolicy: CachePolicy,
    logger: Logger?
): PlatformPlayer = AndroidPlatformPlayer(cachePolicy, logger)

internal class AndroidPlatformPlayer(
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlatformPlayer {
    override fun prepare(item: io.github.msusman.kmplayer.api.MediaItem, autoplay: Boolean) {
        logger?.d("AndroidPlatformPlayer", "prepare: ${item.id}")
    }

    override fun play() {
        logger?.d("AndroidPlatformPlayer", "play")
    }

    override fun pause() {
        logger?.d("AndroidPlatformPlayer", "pause")
    }

    override fun stop() {
        logger?.d("AndroidPlatformPlayer", "stop")
    }

    override fun seekTo(positionMs: Long) {
        logger?.d("AndroidPlatformPlayer", "seekTo: $positionMs")
    }

    override fun setVolume(volume: Float) {
        logger?.d("AndroidPlatformPlayer", "setVolume: $volume")
    }

    override fun setPlaybackSpeed(speed: Float) {
        logger?.d("AndroidPlatformPlayer", "setPlaybackSpeed: $speed")
    }

    override fun release() {
        logger?.d("AndroidPlatformPlayer", "release")
    }
}
