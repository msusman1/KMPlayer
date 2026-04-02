package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlatformContext
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

internal class DefaultPlaybackEngine(
    private val platformContext: PlatformContext?,
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlaybackEngine {
    private val platformPlayer: PlatformPlayer = createPlatformPlayer(
        platformContext = platformContext,
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

    override fun setListener(listener: PlaybackEngine.Listener?) {
        platformPlayer.setListener(object : PlatformPlayer.Listener {
            override fun onBuffering(item: MediaItem, durationMs: Long, positionMs: Long, bufferPercent: Int) {
                listener?.onBuffering(item, durationMs, positionMs, bufferPercent)
            }

            override fun onReady(item: MediaItem, durationMs: Long, positionMs: Long) {
                listener?.onReady(item, durationMs, positionMs)
            }

            override fun onPlaying(item: MediaItem, durationMs: Long, positionMs: Long, speed: Float) {
                listener?.onPlaying(item, durationMs, positionMs, speed)
            }

            override fun onPaused(item: MediaItem, durationMs: Long, positionMs: Long) {
                listener?.onPaused(item, durationMs, positionMs)
            }

            override fun onCompleted(item: MediaItem, durationMs: Long) {
                listener?.onCompleted(item, durationMs)
            }

            override fun onError(item: MediaItem?, error: io.github.msusman.kmplayer.api.PlayerError) {
                listener?.onError(item, error)
            }

            override fun onPosition(positionMs: Long) {
                listener?.onPosition(positionMs)
            }
        })
    }
}
