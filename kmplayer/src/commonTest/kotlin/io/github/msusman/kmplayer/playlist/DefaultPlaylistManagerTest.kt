package io.github.msusman.kmplayer.playlist

import io.github.msusman.kmplayer.api.MediaItem
import io.github.msusman.kmplayer.api.PlaybackMode
import io.github.msusman.kmplayer.api.Playlist
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultPlaylistManagerTest {
    private fun playlistOf(count: Int): Playlist {
        val items = (0 until count).map { index ->
            MediaItem(
                id = "id-$index",
                uri = "file:///track-$index.mp3",
                title = "Track $index"
            )
        }
        return Playlist(id = "pl-1", name = "Test", items = items)
    }

    @Test
    fun setPlaylist_clampsStartIndex() {
        val manager = DefaultPlaylistManager()
        val playlist = playlistOf(3)

        manager.setPlaylist(playlist, startIndex = -5)
        assertEquals("id-0", manager.currentItem()?.id)

        manager.setPlaylist(playlist, startIndex = 99)
        assertEquals("id-2", manager.currentItem()?.id)
    }

    @Test
    fun setPlaylist_emptyPlaylistReturnsNull() {
        val manager = DefaultPlaylistManager()
        manager.setPlaylist(playlistOf(0), startIndex = 0)
        assertNull(manager.currentItem())
    }

    @Test
    fun sequentialPlayback_movesForwardAndBackward() {
        val manager = DefaultPlaylistManager()
        val playlist = playlistOf(3)
        manager.setPlaybackMode(PlaybackMode.SEQUENTIAL)
        manager.setPlaylist(playlist, startIndex = 0)

        assertEquals("id-1", manager.nextItem()?.id)
        assertEquals("id-2", manager.nextItem()?.id)
        assertNull(manager.nextItem())
        assertEquals("id-1", manager.previousItem()?.id)
    }

    @Test
    fun repeatAll_wrapsAround() {
        val manager = DefaultPlaylistManager()
        val playlist = playlistOf(3)
        manager.setPlaybackMode(PlaybackMode.REPEAT_ALL)
        manager.setPlaylist(playlist, startIndex = 2)

        assertEquals("id-0", manager.nextItem()?.id)
        assertEquals("id-2", manager.previousItem()?.id)
    }

    @Test
    fun repeatOne_staysOnSameItem() {
        val manager = DefaultPlaylistManager()
        val playlist = playlistOf(3)
        manager.setPlaybackMode(PlaybackMode.REPEAT_ONE)
        manager.setPlaylist(playlist, startIndex = 1)

        assertEquals("id-1", manager.nextItem()?.id)
        assertEquals("id-1", manager.previousItem()?.id)
    }

    @Test
    fun shuffle_returnsValidItem() {
        val manager = DefaultPlaylistManager()
        val playlist = playlistOf(5)
        manager.setPlaybackMode(PlaybackMode.SHUFFLE)
        manager.setPlaylist(playlist, startIndex = 0)

        val next = manager.nextItem()
        assertNotNull(next)
        assertTrue(playlist.items.any { it.id == next.id })

        val previous = manager.previousItem()
        assertNotNull(previous)
        assertTrue(playlist.items.any { it.id == previous.id })
    }
}
