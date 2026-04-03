package io.github.msusman.kmplayer.api

import io.github.msusman.kmplayer.analytics.AnalyticsTracker
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.core.MusicPlayer
import io.github.msusman.kmplayer.core.MusicPlayerImpl
import io.github.msusman.kmplayer.logging.Logger

class KMPlayer internal constructor(
    private val musicPlayer: MusicPlayer
) {
    fun load(item: MediaItem, autoplay: Boolean = false) = musicPlayer.load(item, autoplay)
    fun setPlaylist(playlist: Playlist, startIndex: Int = 0, autoplay: Boolean = false) =
        musicPlayer.setPlaylist(playlist, startIndex, autoplay)

    fun play() = musicPlayer.play()
    fun pause() = musicPlayer.pause()
    fun stop() = musicPlayer.stop()
    fun seekTo(positionMs: Long) = musicPlayer.seekTo(positionMs)
    fun skipNext() = musicPlayer.skipNext()
    fun skipPrevious() = musicPlayer.skipPrevious()

    fun setPlaybackMode(mode: PlaybackMode) = musicPlayer.setPlaybackMode(mode)
    fun setVolume(volume: Float) = musicPlayer.setVolume(volume)
    fun setPlaybackSpeed(speed: Float) = musicPlayer.setPlaybackSpeed(speed)

    fun currentState(): PlayerState = musicPlayer.currentState()
    fun addListener(listener: PlayerListener) = musicPlayer.addListener(listener)
    fun removeListener(listener: PlayerListener) = musicPlayer.removeListener(listener)

    fun release() = musicPlayer.release()


    class Builder {
        private var config: KMPlayerConfig = KMPlayerConfig()

        fun config(config: KMPlayerConfig) = apply { this.config = config }
        fun cachePolicy(policy: CachePolicy) =
            apply { this.config = config.copy(cachePolicy = policy) }

        fun logger(logger: Logger) = apply { this.config = config.copy(logger = logger) }
        fun analyticsTracker(tracker: AnalyticsTracker) =
            apply { this.config = config.copy(analyticsTracker = tracker) }

        fun defaultPlaybackMode(mode: PlaybackMode) =
            apply { this.config = config.copy(defaultPlaybackMode = mode) }

        fun enableBackgroundPlayback(enabled: Boolean) =
            apply { this.config = config.copy(enableBackgroundPlayback = enabled) }

        fun build(context: Any?): KMPlayer =
            KMPlayer(MusicPlayerImpl(config.copy(context = context)))
    }

}
interface PlayerListener {
    fun onStateChanged(state: PlayerState) {}
    fun onEvent(event: PlayerEvent) {}
}
