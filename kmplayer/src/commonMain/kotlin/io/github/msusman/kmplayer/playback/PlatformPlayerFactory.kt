package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.cache.CachePolicy
import io.github.msusman.kmplayer.logging.Logger

expect fun createPlatformPlayer(
    context: Any?,
    cachePolicy: CachePolicy,
    logger: Logger?
): PlatformPlayer
