package io.github.msusman.kmplayer.cache

interface CacheManager {
    fun getCachedUri(remoteUri: String): String?
    fun cache(remoteUri: String): String?
    fun clear()
}
