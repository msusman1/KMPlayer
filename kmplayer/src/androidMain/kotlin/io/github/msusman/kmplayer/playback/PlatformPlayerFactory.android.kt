package io.github.msusman.kmplayer.playback

import android.net.Uri
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlatformContext
import io.github.msusman.kmplayer.api.PlayerError
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

actual fun createPlatformPlayer(
    platformContext: PlatformContext?,
    cachePolicy: CachePolicy,
    logger: Logger?
): PlatformPlayer = AndroidPlatformPlayer(
    platformContext = platformContext,
    cachePolicy = cachePolicy,
    logger = logger
)

internal class AndroidPlatformPlayer(
    platformContext: PlatformContext?,
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlatformPlayer {
    private val context = requireNotNull(platformContext) {
        "Android PlatformContext is required to initialize ExoPlayer."
    }.context.applicationContext

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private var listener: PlatformPlayer.Listener? = null
    private var currentItem: MediaItem? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                val item = currentItem ?: return
                val duration = safeDuration()
                val position = player.currentPosition.coerceAtLeast(0L)
                when (state) {
                    Player.STATE_BUFFERING -> {
                        listener?.onBuffering(item, duration, position, player.bufferedPercentage)
                    }
                    Player.STATE_READY -> {
                        listener?.onReady(item, duration, position)
                    }
                    Player.STATE_ENDED -> {
                        listener?.onCompleted(item, duration)
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                val item = currentItem ?: return
                val duration = safeDuration()
                val position = player.currentPosition.coerceAtLeast(0L)
                if (isPlaying) {
                    listener?.onPlaying(item, duration, position, player.playbackParameters.speed)
                } else {
                    listener?.onPaused(item, duration, position)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                val item = currentItem
                listener?.onError(item, PlayerError.Unknown(error.message, error))
            }
        })
    }

    override fun prepare(item: MediaItem, autoplay: Boolean) {
        logger?.d("AndroidPlatformPlayer", "prepare: ${item.id}")
        currentItem = item
        val mediaItem = ExoMediaItem.fromUri(Uri.parse(item.uri))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = autoplay
    }

    override fun play() {
        logger?.d("AndroidPlatformPlayer", "play")
        player.playWhenReady = true
    }

    override fun pause() {
        logger?.d("AndroidPlatformPlayer", "pause")
        player.pause()
    }

    override fun stop() {
        logger?.d("AndroidPlatformPlayer", "stop")
        player.stop()
    }

    override fun seekTo(positionMs: Long) {
        logger?.d("AndroidPlatformPlayer", "seekTo: $positionMs")
        player.seekTo(positionMs)
    }

    override fun setVolume(volume: Float) {
        logger?.d("AndroidPlatformPlayer", "setVolume: $volume")
        player.volume = volume
    }

    override fun setPlaybackSpeed(speed: Float) {
        logger?.d("AndroidPlatformPlayer", "setPlaybackSpeed: $speed")
        player.playbackParameters = PlaybackParameters(speed)
    }

    override fun release() {
        logger?.d("AndroidPlatformPlayer", "release")
        player.release()
    }

    override fun setListener(listener: PlatformPlayer.Listener?) {
        this.listener = listener
    }

    private fun safeDuration(): Long {
        val duration = player.duration
        return if (duration < 0) 0L else duration
    }
}
