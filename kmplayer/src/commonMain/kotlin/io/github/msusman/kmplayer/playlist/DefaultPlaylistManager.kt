package io.github.msusman.kmplayer.playlist

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlaybackMode
import io.github.msusman.kmplayer.api.Playlist
import io.github.msusman.kmplayer.playback.PlaybackModeStrategy
import io.github.msusman.kmplayer.playback.RepeatAllStrategy
import io.github.msusman.kmplayer.playback.RepeatOnceStrategy
import io.github.msusman.kmplayer.playback.SequentialStrategy
import io.github.msusman.kmplayer.playback.ShuffleStrategy

internal class DefaultPlaylistManager : PlaylistManager {
    private var playlist: Playlist = Playlist(id = "")
    private var index: Int = -1
    private var mode: PlaybackMode = PlaybackMode.SEQUENTIAL
    private var strategy: PlaybackModeStrategy = SequentialStrategy

    override fun setPlaylist(playlist: Playlist, startIndex: Int) {
        this.playlist = playlist
        this.index = startIndex.coerceIn(playlist.items.indices)
    }

    override fun currentItem(): MediaItem? = playlist.items.getOrNull(index)

    override fun nextItem(): MediaItem? {
        val next = strategy.nextIndex(index, playlist)
        if (next < 0) return null
        index = next
        return currentItem()
    }

    override fun previousItem(): MediaItem? {
        val previous = strategy.previousIndex(index, playlist)
        if (previous < 0) return null
        index = previous
        return currentItem()
    }

    override fun setPlaybackMode(mode: PlaybackMode) {
        this.mode = mode
        this.strategy = when (mode) {
            PlaybackMode.SEQUENTIAL -> SequentialStrategy
            PlaybackMode.SHUFFLE -> ShuffleStrategy
            PlaybackMode.REPEAT_ONE -> RepeatOnceStrategy
            PlaybackMode.REPEAT_ALL -> RepeatAllStrategy
        }
    }
}
