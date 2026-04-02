package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.Playlist

interface PlaybackModeStrategy {
    fun nextIndex(currentIndex: Int, playlist: Playlist): Int
    fun previousIndex(currentIndex: Int, playlist: Playlist): Int
}

object SequentialStrategy : PlaybackModeStrategy {
    override fun nextIndex(currentIndex: Int, playlist: Playlist): Int {
        val next = currentIndex + 1
        return if (next < playlist.items.size) next else -1
    }

    override fun previousIndex(currentIndex: Int, playlist: Playlist): Int {
        val previous = currentIndex - 1
        return if (previous >= 0) previous else -1
    }
}

object ShuffleStrategy : PlaybackModeStrategy {
    override fun nextIndex(currentIndex: Int, playlist: Playlist): Int {
        if (playlist.items.isEmpty()) return -1
        return (playlist.items.indices).random()
    }

    override fun previousIndex(currentIndex: Int, playlist: Playlist): Int {
        if (playlist.items.isEmpty()) return -1
        return (playlist.items.indices).random()
    }
}

object RepeatOnceStrategy : PlaybackModeStrategy {
    override fun nextIndex(currentIndex: Int, playlist: Playlist): Int = currentIndex
    override fun previousIndex(currentIndex: Int, playlist: Playlist): Int = currentIndex
}

object RepeatAllStrategy : PlaybackModeStrategy {
    override fun nextIndex(currentIndex: Int, playlist: Playlist): Int {
        if (playlist.items.isEmpty()) return -1
        val next = currentIndex + 1
        return if (next < playlist.items.size) next else 0
    }

    override fun previousIndex(currentIndex: Int, playlist: Playlist): Int {
        if (playlist.items.isEmpty()) return -1
        val previous = currentIndex - 1
        return if (previous >= 0) previous else playlist.items.lastIndex
    }
}
