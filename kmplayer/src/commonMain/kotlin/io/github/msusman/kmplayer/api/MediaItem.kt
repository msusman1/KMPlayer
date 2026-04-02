package io.github.msusman.kmplayer.api

data class MediaItem(
    val id: String,
    val uri: String,
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val durationMs: Long? = null,
    val artworkUri: String? = null,
    val isLocal: Boolean = false,
    val extras: Map<String, String> = emptyMap()
)
