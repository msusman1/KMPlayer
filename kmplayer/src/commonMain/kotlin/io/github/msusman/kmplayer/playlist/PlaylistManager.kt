package io.github.msusman.kmplayer.playlist

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlaybackMode
import io.github.msusman.kmplayer.api.Playlist

interface PlaylistManager {
    fun setPlaylist(playlist: Playlist, startIndex: Int)
    fun currentItem(): MediaItem?
    fun nextItem(): MediaItem?
    fun previousItem(): MediaItem?
    fun setPlaybackMode(mode: PlaybackMode)
}
