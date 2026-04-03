package io.github.msusman.kmplayer.playback

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.Playlist
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaybackModeStrategyTest {
    private fun playlistOf(count: Int): Playlist {
        val items = (0 until count).map { index ->
            MediaItem(
                id = "id-$index",
                uri = "file:///track-$index.mp3"
            )
        }
        return Playlist(id = "pl-1", items = items)
    }

    @Test
    fun sequentialStrategy_respectsBounds() {
        val playlist = playlistOf(2)
        assertEquals(1, SequentialStrategy.nextIndex(0, playlist))
        assertEquals(-1, SequentialStrategy.nextIndex(1, playlist))
        assertEquals(0, SequentialStrategy.previousIndex(1, playlist))
        assertEquals(-1, SequentialStrategy.previousIndex(0, playlist))
    }

    @Test
    fun repeatAllStrategy_wrapsAround() {
        val playlist = playlistOf(3)
        assertEquals(0, RepeatAllStrategy.nextIndex(2, playlist))
        assertEquals(2, RepeatAllStrategy.previousIndex(0, playlist))
    }

    @Test
    fun repeatOnceStrategy_keepsIndex() {
        val playlist = playlistOf(3)
        assertEquals(1, RepeatOnceStrategy.nextIndex(1, playlist))
        assertEquals(1, RepeatOnceStrategy.previousIndex(1, playlist))
    }

    @Test
    fun shuffleStrategy_emptyPlaylistReturnsMinusOne() {
        val empty = playlistOf(0)
        assertEquals(-1, ShuffleStrategy.nextIndex(0, empty))
        assertEquals(-1, ShuffleStrategy.previousIndex(0, empty))
    }

    @Test
    fun shuffleStrategy_returnsValidIndexForNonEmpty() {
        val playlist = playlistOf(5)
        val next = ShuffleStrategy.nextIndex(0, playlist)
        val previous = ShuffleStrategy.previousIndex(0, playlist)
        assertTrue(next in playlist.items.indices)
        assertTrue(previous in playlist.items.indices)
    }
}
