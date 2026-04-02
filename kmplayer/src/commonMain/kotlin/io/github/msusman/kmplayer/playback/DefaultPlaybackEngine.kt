package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

internal class DefaultPlaybackEngine(
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlaybackEngine {
    private val platformPlayer: PlatformPlayer = createPlatformPlayer(
        cachePolicy = cachePolicy,
        logger = logger
    )

    override fun prepare(item: MediaItem, autoplay: Boolean) {
        logger?.d("DefaultPlaybackEngine", "prepare: ${item.id}")
        platformPlayer.prepare(item, autoplay)
    }

    override fun play() {
        logger?.d("DefaultPlaybackEngine", "play")
        platformPlayer.play()
    }

    override fun pause() {
        logger?.d("DefaultPlaybackEngine", "pause")
        platformPlayer.pause()
    }

    override fun stop() {
        logger?.d("DefaultPlaybackEngine", "stop")
        platformPlayer.stop()
    }

    override fun seekTo(positionMs: Long) {
        logger?.d("DefaultPlaybackEngine", "seekTo: $positionMs")
        platformPlayer.seekTo(positionMs)
    }

    override fun setVolume(volume: Float) {
        logger?.d("DefaultPlaybackEngine", "setVolume: $volume")
        platformPlayer.setVolume(volume)
    }

    override fun setPlaybackSpeed(speed: Float) {
        logger?.d("DefaultPlaybackEngine", "setPlaybackSpeed: $speed")
        platformPlayer.setPlaybackSpeed(speed)
    }

    override fun release() {
        logger?.d("DefaultPlaybackEngine", "release")
        platformPlayer.release()
    }
}
