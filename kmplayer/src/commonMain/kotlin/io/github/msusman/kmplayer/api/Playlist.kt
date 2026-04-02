package io.github.msusman.kmplayer.api

data class Playlist(
    val id: String,
    val name: String? = null,
    val items: List<MediaItem> = emptyList()
)
