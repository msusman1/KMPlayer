package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlayerError
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemPlaybackStalledNotification
import platform.AVFoundation.AVPlayerItemFailedToPlayToEndTimeNotification
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.seekToTime
import platform.AVFoundation.volume
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.darwin.NSObject

actual fun createPlatformPlayer(
    platformContext: Any?,
    cachePolicy: CachePolicy,
    logger: Logger?
): PlatformPlayer = IOSPlatformPlayer(cachePolicy, logger)

internal class IOSPlatformPlayer(
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlatformPlayer {
    private var player: AVPlayer? = null
    private var currentItem: MediaItem? = null
    private var listener: PlatformPlayer.Listener? = null
    private var endObserver: NSObject? = null
    private var failObserver: NSObject? = null
    private var stallObserver: NSObject? = null

    override fun prepare(item: MediaItem, autoplay: Boolean) {
        logger?.d("IOSPlatformPlayer", "prepare: ${item.id}")
        currentItem = item
        val url = NSURL.URLWithString(item.uri)
        if (url == null) {
            listener?.onError(item, PlayerError.SourceUnavailable(item.uri, "Invalid URL"))
            return
        }
        val playerItem = AVPlayerItem(URL = url)
        attachObservers(playerItem)
        val avPlayer = AVPlayer(playerItem = playerItem)
        player = avPlayer

        val durationMs = durationMs(playerItem)
        listener?.onReady(item, durationMs, positionMs = 0L)

        if (autoplay) play()
    }

    override fun play() {
        logger?.d("IOSPlatformPlayer", "play")
        val item = currentItem ?: return
        player?.play()
        listener?.onPlaying(item, durationMs(player?.currentItem), positionMs(), speed = 1f)
    }

    override fun pause() {
        logger?.d("IOSPlatformPlayer", "pause")
        val item = currentItem ?: return
        player?.pause()
        listener?.onPaused(item, durationMs(player?.currentItem), positionMs())
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun stop() {
        logger?.d("IOSPlatformPlayer", "stop")
        val item = currentItem ?: return
        player?.pause()
        player?.seekToTime(platform.CoreMedia.CMTimeMake(value = 0, timescale = 1))
        listener?.onPaused(item, durationMs(player?.currentItem), 0L)
    }
    @OptIn(ExperimentalForeignApi::class)
    override fun seekTo(positionMs: Long) {
        logger?.d("IOSPlatformPlayer", "seekTo: $positionMs")
        val time = platform.CoreMedia.CMTimeMake(value = positionMs, timescale = 1000)
        player?.seekToTime(time)
    }

    override fun setVolume(volume: Float) {
        logger?.d("IOSPlatformPlayer", "setVolume: $volume")
        player?.volume = volume
    }

    override fun setPlaybackSpeed(speed: Float) {
        logger?.d("IOSPlatformPlayer", "setPlaybackSpeed: $speed")
        player?.rate = speed
    }

    override fun release() {
        logger?.d("IOSPlatformPlayer", "release")
        detachObservers()
        player?.pause()
        player = null
    }

    override fun setListener(listener: PlatformPlayer.Listener?) {
        this.listener = listener
    }

    private fun attachObservers(playerItem: AVPlayerItem) {
        detachObservers()
        val center = NSNotificationCenter.defaultCenter
        endObserver = center.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = playerItem,
            queue = null
        ) { _: NSNotification? ->
            val item = currentItem ?: return@addObserverForName
            listener?.onCompleted(item, durationMs(playerItem))
        }

        failObserver = center.addObserverForName(
            name = AVPlayerItemFailedToPlayToEndTimeNotification,
            `object` = playerItem,
            queue = null
        ) { _: NSNotification? ->
            val item = currentItem
            listener?.onError(item, PlayerError.DecodeFailure("Playback failed"))
        }

        stallObserver = center.addObserverForName(
            name = AVPlayerItemPlaybackStalledNotification,
            `object` = playerItem,
            queue = null
        ) { _: NSNotification? ->
            val item = currentItem ?: return@addObserverForName
            listener?.onBuffering(item, durationMs(playerItem), positionMs(), bufferPercent = 0)
        }
    }

    private fun detachObservers() {
        val center = NSNotificationCenter.defaultCenter
        endObserver?.let { center.removeObserver(it) }
        failObserver?.let { center.removeObserver(it) }
        stallObserver?.let { center.removeObserver(it) }
        endObserver = null
        failObserver = null
        stallObserver = null
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun durationMs(item: AVPlayerItem?): Long {
        if (item == null) return 0L
        val seconds = CMTimeGetSeconds(item.duration)
        if (seconds.isNaN() || seconds.isInfinite()) return 0L
        return (seconds * 1000.0).toLong()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun positionMs(): Long {
        val avPlayer = player ?: return 0L
        val seconds = CMTimeGetSeconds(avPlayer.currentTime())
        if (seconds.isNaN() || seconds.isInfinite()) return 0L
        return (seconds * 1000.0).toLong()
    }
}
