package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlayerError
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.volume
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.Foundation.addObserver
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual fun createPlatformPlayer(
    context: Any?,
    cachePolicy: CachePolicy,
    logger: Logger?
): PlatformPlayer = IOSPlatformPlayer(cachePolicy, logger)


@OptIn(ExperimentalForeignApi::class)
internal class IOSPlatformPlayer(
    private val cachePolicy: CachePolicy,
    private val logger: Logger?
) : PlatformPlayer {

    private var player: AVPlayer? = null
    private var playerItem: AVPlayerItem? = null

    private var listener: PlatformPlayer.Listener? = null
    private var currentItem: MediaItem? = null

    private var timeObserver: Any? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // MARK: Prepare

    override fun prepare(item: MediaItem, autoplay: Boolean) {
        logger?.d("IOSPlatformPlayer", "prepare: ${item.id}")

        currentItem = item

        val url = NSURL.URLWithString(item.uri)
        if (url == null) {
            listener?.onError(item, PlayerError.Unknown("Invalid URL", null))
            return
        }

        playerItem = AVPlayerItem(url)
        player = AVPlayer(playerItem = playerItem)

        observePlayerItem()
        startProgressUpdates()

        if (autoplay) {
            play()
        }
    }

    // MARK: Playback Controls

    override fun play() {
        logger?.d("IOSPlatformPlayer", "play")
        player?.play()
        notifyPlaying()
    }

    override fun pause() {
        logger?.d("IOSPlatformPlayer", "pause")
        player?.pause()
        notifyPaused()
    }

    override fun stop() {
        logger?.d("IOSPlatformPlayer", "stop")
        player?.pause()
        player?.replaceCurrentItemWithPlayerItem(null)
    }

    override fun seekTo(positionMs: Long) {
        logger?.d("IOSPlatformPlayer", "seekTo: $positionMs")

        val time = CMTimeMake(value = positionMs, timescale = 1000)
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

        timeObserver?.let {
            player?.removeTimeObserver(it)
            timeObserver = null
        }

        scope.cancel()

        player?.pause()
        player = null
        playerItem = null
    }

    override fun setListener(listener: PlatformPlayer.Listener?) {
        this.listener = listener
    }

    // MARK: Observers

    private fun observePlayerItem() {
        val item = playerItem ?: return

        item.addObserver(
            observer = object : NSObject() {},
            forKeyPath = "status",
            options = NSKeyValueObservingOptionNew,
            context = null
        )

        // Completion
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = item,
            queue = null
        ) {
            val media = currentItem ?: return@addObserverForName
            listener?.onCompleted(media, durationMs())
        }
    }

    private fun startProgressUpdates() {
        val interval = CMTimeMake(value = 500, timescale = 1000)

        timeObserver = player?.addPeriodicTimeObserverForInterval(
            interval = interval,
            queue = dispatch_get_main_queue()
        ) { time ->

            val position = CMTimeGetSeconds(time) * 1000
            listener?.onPosition(position.toLong())
        }
    }

    // MARK: State Notifications

    private fun notifyPlaying() {
        val item = currentItem ?: return
        listener?.onPlaying(
            item,
            durationMs(),
            positionMs(),
            player?.rate ?: 1f
        )
    }

    private fun notifyPaused() {
        val item = currentItem ?: return
        listener?.onPaused(
            item,
            durationMs(),
            positionMs()
        )
    }

    // MARK: Helpers


    private fun durationMs(): Long {
        val duration = playerItem?.duration ?: return 0L
        val seconds = CMTimeGetSeconds(duration)
        return if (seconds.isNaN()) 0L else (seconds * 1000).toLong()
    }

    private fun positionMs(): Long {
        val time = player?.currentTime() ?: return 0L
        val seconds = CMTimeGetSeconds(time)
        return if (seconds.isNaN()) 0L else (seconds * 1000).toLong()
    }
}
