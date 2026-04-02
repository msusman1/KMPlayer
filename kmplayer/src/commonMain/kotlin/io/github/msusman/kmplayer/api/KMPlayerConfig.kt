package io.github.msusman.kmplayer.api

import io.github.msusman.kmplayer.analytics.AnalyticsTracker
import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

data class KMPlayerConfig(
    val platformContext: Any? = null,
    val cachePolicy: CachePolicy = CachePolicy(),
    val enableBackgroundPlayback: Boolean = true,
    val defaultPlaybackMode: PlaybackMode = PlaybackMode.SEQUENTIAL,
    val logger: Logger? = null,
    val analyticsTracker: AnalyticsTracker? = null
)
