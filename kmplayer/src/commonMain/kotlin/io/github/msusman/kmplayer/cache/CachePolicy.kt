package io.github.msusman.kmplayer.cache

data class CachePolicy(
    val enableCaching: Boolean = true,
    val maxCacheBytes: Long = 200L * 1024L * 1024L,
    val allowCellular: Boolean = true
)
