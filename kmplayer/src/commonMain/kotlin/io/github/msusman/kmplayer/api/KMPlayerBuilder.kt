package io.github.msusman.kmplayer.api

import io.github.msusman.kmplayer.analytics.AnalyticsTracker
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.core.MusicPlayerImpl
import io.github.msusman.kmplayer.logging.Logger

class KMPlayerBuilder {
    private var config: KMPlayerConfig = KMPlayerConfig()

    fun config(config: KMPlayerConfig) = apply { this.config = config }
    fun cachePolicy(policy: CachePolicy) = apply { this.config = config.copy(cachePolicy = policy) }
    fun logger(logger: Logger) = apply { this.config = config.copy(logger = logger) }
    fun analyticsTracker(tracker: AnalyticsTracker) = apply { this.config = config.copy(analyticsTracker = tracker) }
    fun defaultPlaybackMode(mode: PlaybackMode) = apply { this.config = config.copy(defaultPlaybackMode = mode) }
    fun enableBackgroundPlayback(enabled: Boolean) =
        apply { this.config = config.copy(enableBackgroundPlayback = enabled) }

    fun build(): KMPlayer = KMPlayer(MusicPlayerImpl(config))
}
